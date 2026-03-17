import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { LifeeventStore } from '../../../lifeevents/state/lifeevent-store';
import { Policy } from '../../../policies/state/policies-api';
import { PolicyStatus } from '../../../policies/state/policy-status.enum';

@Component({
	selector: 'app-create-policy-dialog',
	imports: [FormsModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
	standalone: true,
	templateUrl: './create-policy-dialog.html',
	styleUrl: './create-policy-dialog.css',
})
export class CreatePolicyDialog {
	policy = inject(MAT_DIALOG_DATA) as Policy;
	dialogRef = inject(MatDialogRef<CreatePolicyDialog>);
	lifeEventStore = inject(LifeeventStore);

	id: number | null = this.policy?.id ?? null;
	name: string = this.policy?.name ?? '';
	premium: number | null = this.policy?.premium ?? null;
	selectedLifeEvents: number[] = this.policy?.coveredLifeEvents ?? [];
	status: PolicyStatus | null = this.policy?.status ?? null;

	onLifeEventToggle(id: number, event: any) {
		if (event.target.checked) {
			this.selectedLifeEvents.push(id);
		} else {
			this.selectedLifeEvents = this.selectedLifeEvents.filter((e) => e !== id);
		}
	}

	submit() {
		this.dialogRef.close({
			id: this.id,
			name: this.name,
			premium: this.premium,
			coveredLifeEvents: this.selectedLifeEvents,
			status: this.status,
		});
	}

	close() {
		this.dialogRef.close();
	}
}
