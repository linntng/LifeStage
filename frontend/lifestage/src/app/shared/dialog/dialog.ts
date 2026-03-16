import { Component, inject, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-policy-dialog',
	imports: [MatDialogModule, MatButtonModule],
	template: `
		<h2 mat-dialog-title>{{ data.title }}</h2>

		<mat-dialog-content>
			{{data.content}}
		</mat-dialog-content>

		<mat-dialog-actions>
			<button mat-button mat-dialog-actions (click)="confirm()">Confirm</button>
			<button mat-button mat-dialog-close>Close</button>
		</mat-dialog-actions>
	`,
})
export class Dialog {
	data = inject(MAT_DIALOG_DATA);
	dialogRef = inject(MatDialogRef<Dialog>);

	confirm() {
		this.dialogRef.close(true);
	}

	
}

