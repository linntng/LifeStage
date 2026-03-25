import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { Case } from '../../state/case-api';
import { PoliciesStore } from '../../../policies/state/policies-store';
import { User } from '../../../user/user-state/user-api';
import { LifeeventStore } from '../../../lifeevents/state/lifeevent-store';
import { CommonModule } from '@angular/common';

type CaseDialogData = {
	case: Case;
	user: User;
};

@Component({
	selector: 'app-case-dialog',
	imports: [MatDialogModule, MatButtonModule, CommonModule],
	template: `
		<h2 mat-dialog-title>Review case {{ case.id }}</h2>

		<mat-dialog-content class="flex flex-col gap-4">
			@if (policy) {
				<p class="text-sm text-gray-700 m-3">
					The user <i>{{ case.userId }}</i> is applying for
					<strong>{{ policy.name }}</strong>
				</p>

				<!-- Comparison section -->
				<div class="grid grid-cols-2 gap-4">
					<!-- User life events -->
					<div>
						<p class="text-xs uppercase tracking-wide text-gray-500 mb-2">
							User life events
						</p>

						<ul class="space-y-1 text-sm">
							@for (eventId of userLifeEvents; track eventId) {
								<li class="flex items-center gap-2">
									<span
										class="h-2 w-2 rounded-full"
										[ngClass]="
											isCovered(eventId) ? 'bg-green-500' : 'bg-red-500'
										"
									></span>
									{{ getLifeEventName(eventId) }}
								</li>
							}
						</ul>
						@if (userLifeEvents.length === 0) {
							<p class="text-sm text-gray-500 italic">User has no life events</p>
						}
					</div>

					<!-- Policy coverage -->
					<div>
						<p class="text-xs uppercase tracking-wide text-gray-500 mb-2">
							Policy coverage
						</p>

						<ul class="space-y-1 text-sm">
							@for (eventId of policyLifeEvents; track eventId) {
								<li class="flex items-center gap-2">
									<span
										class="h-2 w-2 rounded-full"
										[ngClass]="
											isExtra(eventId) ? 'bg-gray-400' : 'bg-green-500'
										"
									></span>
									{{ getLifeEventName(eventId) }}
								</li>
							}
						</ul>
						@if (policyLifeEvents.length === 0) {
							<p class="text-sm text-gray-500 italic">User has no policies</p>
						}
					</div>
				</div>

				<!-- Legend -->
				<div class="flex gap-4 text-xs text-gray-600 pt-2 border-t">
					<span class="flex items-center gap-1">
						<span class="h-2 w-2 rounded-full bg-green-500"></span>
						Covered
					</span>
					<span class="flex items-center gap-1">
						<span class="h-2 w-2 rounded-full bg-red-500"></span>
						Missing coverage
					</span>
					<span class="flex items-center gap-1">
						<span class="h-2 w-2 rounded-full bg-gray-400"></span>
						Not relevant
					</span>
				</div>
			} @else {
				<p>No policy with this ID</p>
			}
		</mat-dialog-content>

		<mat-dialog-actions>
			<button (click)="accept()" class="bg-green-200 hover:bg-green-400 m-1">Accept</button>
			<button (click)="deny()" class="bg-red-200 hover:bg-red-400 m-1">Deny</button>
			<button mat-dialog-close class="bg-gray-200 hover:bg-gray-400 m-1">Close</button>
		</mat-dialog-actions>
	`,
})
export class CaseDialog {
	dialogRef = inject(MatDialogRef<CaseDialog>);
	data = inject<CaseDialogData>(MAT_DIALOG_DATA);

	case = this.data.case;
	user = this.data.user;

	policyStore = inject(PoliciesStore);
	lifeEventStore = inject(LifeeventStore);

	lifeEvents = this.lifeEventStore.lifeevents();
	policies = this.policyStore.policies();

	policy = this.policies.find((policy) => policy.id === this.case.policyId);

	// ✅ Derived data (safe, local only)
	get userLifeEvents() {
		return this.user?.lifeEventIds ?? [];
	}

	get policyLifeEvents() {
		return this.policy?.coveredLifeEvents ?? [];
	}

	isCovered(eventId: number) {
		return this.policyLifeEvents.includes(eventId);
	}

	isExtra(eventId: number) {
		return !this.userLifeEvents.includes(eventId);
	}

	getLifeEventName(id: number) {
		return this.lifeEvents.find((e) => e.id === id)?.name ?? id;
	}

	accept() {
		this.dialogRef.close('ACCEPTED');
	}

	deny() {
		this.dialogRef.close('DENIED');
	}
}
