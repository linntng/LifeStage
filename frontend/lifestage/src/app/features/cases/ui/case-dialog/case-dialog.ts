import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { Case } from '../../state/case-api';
import { PoliciesStore } from '../../../policies/state/policies-store';

@Component({
	selector: 'app-case-dialog',
	imports: [MatDialogModule, MatButtonModule],
	template: `
		<h2 mat-dialog-title>Review case {{ case.id }}</h2>

		<mat-dialog-content>
				@if (policy) {
					<p>The user is applying for the policy <strong>{{ policy.name }}</strong></p>
				} @else {
					<p>No policy with this ID</p>
				}
		</mat-dialog-content>

		<mat-dialog-actions>
			<button mat-button mat-dialog-actions (click)="confirm()">Confirm</button>
			<button mat-button mat-dialog-close>Close</button>
		</mat-dialog-actions>
	`,
})
export class CaseDialog {
	dialogRef = inject(MatDialogRef<CaseDialog>);
	case = inject<Case>(MAT_DIALOG_DATA);
	policyStore = inject(PoliciesStore);
	policies = this.policyStore.policies();
	policy = this.policies.find((policy) => policy.id === this.case.policyId);

	confirm() {
		this.dialogRef.close(true);
	}
}
