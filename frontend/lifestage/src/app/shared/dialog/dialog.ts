import { Component, inject } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
	selector: 'app-policy-dialog',
	imports: [MatDialogModule, MatButtonModule],
	template: `
		<h2 mat-dialog-title>{{ data.title }}</h2>

		<mat-dialog-content>
			{{ data.content }}
		</mat-dialog-content>

		<mat-dialog-actions>
			<button class=" m-1 bg-green-200 hover:bg-green-400" (click)="confirm()">
				Confirm
			</button>
			<button mat-dialog-close class="bg-gray-200 hover:bg-gray-400 m-1">Close</button>
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
