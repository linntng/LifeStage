import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { LifeeventStore } from '../../../lifeevents/state/lifeevent-store';
import { Policy } from '../../../policies/state/policies-api';
import { PolicyStatus } from '../../../policies/state/policy-status.enum';

@Component({
	selector: 'app-review-policy-dialog',
	standalone: true,
	templateUrl: './review-policy-dialog.html',
})
export class ReviewPolicyDialog {
	policy = inject(MAT_DIALOG_DATA) as Policy;
	dialogRef = inject(MatDialogRef<ReviewPolicyDialog>);
	lifeEventStore = inject(LifeeventStore);

	id = this.policy?.id ?? null;
	name = this.policy?.name ?? '';
	premium = this.policy?.premium ?? null;
	selectedLifeEvents = this.policy?.coveredLifeEvents ?? [];
	status = this.policy?.status ?? null;
	inReview = this.policy?.inReview ?? null;

	back() {
		this.dialogRef.close();
	}

	deny() {
		this.dialogRef.close({
			id: this.id,
			name: this.name,
			premium: this.premium,
			coveredLifeEvents: this.selectedLifeEvents,
			status: PolicyStatus.CANCELLED,
			inReview: this.inReview,
		});
	}

	accept() {
		this.dialogRef.close({
			id: this.id,
			name: this.name,
			premium: this.premium,
			coveredLifeEvents: this.selectedLifeEvents,
			status: PolicyStatus.ACTIVE,
			inReview: this.inReview,
		});
	}
}
