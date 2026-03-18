import { Component, inject } from '@angular/core';
import { PoliciesStore } from '../policies/state/policies-store';
import { PolicyStatus } from '../policies/state/policy-status.enum';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { CreatePolicyDialog } from './policy-dialogs/create-policy-dialog/create-policy-dialog';
import { Policy } from '../policies/state/policies-api';
import { ReviewPolicyDialog } from './policy-dialogs/review-policy-dialog/review-policy-dialog';

@Component({
	selector: 'app-policy-manager',
	imports: [MatDialogModule, CreatePolicyDialog],
	templateUrl: './policy-manager.html',
	styleUrl: './policy-manager.css',
})
export class PolicyManager {
	policyStore = inject(PoliciesStore);
	dialog = inject(MatDialog);

	getActivePolicies() {
		return this.policyStore.getPoliciesFilteredByStatus([PolicyStatus.ACTIVE]);
	}

	getReviewPolicies() {
		return this.policyStore.policiesToReview();
	}

	createOrUpdatePolicyDialog(policy?: Policy) {
		const dialogRef = this.dialog.open(CreatePolicyDialog, {
			width: '600px',
			data: policy,
		});
		dialogRef.afterClosed().subscribe((resultPolicy) => {
			if (!resultPolicy) {
				return;
			} else if (!policy) {
				this.policyStore.postPolicy(resultPolicy);
			} else {
				this.policyStore.patchPolicy(resultPolicy);
			}
		});
	}

	reviewPolicyDialog(policy: Policy) {
		const dialogRef = this.dialog.open(ReviewPolicyDialog, {
			width: '600px',
			data: policy,
		});
		dialogRef.afterClosed().subscribe((resultPolicy) => {
			this.policyStore.patchPolicy(resultPolicy);
		});
	}
}
