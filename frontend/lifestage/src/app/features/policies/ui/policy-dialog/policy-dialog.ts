import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { Policy } from '../../state/policies-api';

@Component({
	selector: 'app-policy-dialog',
	imports: [MatDialogModule, MatButtonModule],
	template: `
		<h2 mat-dialog-title>Request policy</h2>

		<mat-dialog-content>
			Do you want to request {{ data.name }}? Once requested, it will be sent for approval.
		</mat-dialog-content>

		<mat-dialog-actions>
			<button mat-button mat-dialog-actions (click)="confirm()">Confirm</button>
			<button mat-button mat-dialog-close>Close</button>
		</mat-dialog-actions>
	`,
})
export class PolicyDialog {
	data = inject<Policy>(MAT_DIALOG_DATA);
	dialogRef = inject(MatDialogRef<PolicyDialog>);

	confirm() {
		this.dialogRef.close(true);
	}
}
