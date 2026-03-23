import { Component, computed, inject, OnInit } from '@angular/core';
import { PoliciesStore } from '../policies/state/policies-store';
import { PolicyStatus } from '../policies/state/policy-status.enum';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CreatePolicyDialog } from './policy-dialogs/create-policy-dialog/create-policy-dialog';
import { Policy } from '../policies/state/policies-api';
import { ReviewPolicyDialog } from './policy-dialogs/review-policy-dialog/review-policy-dialog';
import { LifeeventStore } from '../lifeevents/state/lifeevent-store';

@Component({
	selector: 'app-policy-manager',
	imports: [MatDialogModule],
	templateUrl: './policy-manager.html',
})
export class PolicyManager implements OnInit {
	policyStore = inject(PoliciesStore);
	lifeeventStore = inject(LifeeventStore);
	dialog = inject(MatDialog);
	tab: 'active' | 'review' | 'rejected' = 'active';
	selectedRejectedPolicy: any = null;

	ngOnInit(): void {
		this.policyStore.loadPolicies();
		this.lifeeventStore.loadLifeevents();
	}
	getActivePolicies() {
		return this.policyStore.getPoliciesFilteredByStatus([PolicyStatus.ACTIVE]);
	}

	reviewPolicies = this.policyStore.policiesToReview;

	rejectedPoliciesWithExplanation = computed(() => {
		const rejected = this.policyStore.policiesRejected();
		const all = this.policyStore.policies();
		if (rejected) {
			const rejectionMap = new Map(
				rejected
					.filter((r) => r.rejectedPolicyId !== null)
					.map((r) => [r.rejectedPolicyId, r.explanation]),
			);

			return all
				.filter((p) => rejectionMap.has(p.id))
				.map((p) => ({
					...p,
					explanation: rejectionMap.get(p.id),
				}));
		} else {
			return [];
		}
	});

	createOrUpdatePolicyDialog(policy?: Policy) {
		const dialogRef = this.dialog.open(CreatePolicyDialog, {
			width: '600px',
			data: policy,
		});
		dialogRef.afterClosed().subscribe((result) => {
			if (!result) {
				return;
			} else if (!policy) {
				this.policyStore.postPolicy(result.policy);
			} else {
				this.policyStore.patchPolicy(result.policy, result.rejection);
			}
		});
	}

	reviewPolicyDialog(policy: Policy) {
		const dialogRef = this.dialog.open(ReviewPolicyDialog, {
			width: '600px',
			data: policy,
		});
		dialogRef.afterClosed().subscribe((result) => {
			if (!result) {
				return;
			} else {
				this.policyStore.patchPolicy(result.policy, result.rejection);
			}
		});
	}
}
