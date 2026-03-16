import { Component, inject, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDialog } from '@angular/material/dialog';

@Component({
	selector: 'app-info-card',
	imports: [MatCardModule],
	templateUrl: './info-card.html',
})
export class InfoCard {
	@Input() title = '';
	@Input() color = 'bg-primary';

	private dialog = inject(MatDialog);


	openDialog() {
	// 	const dialogRef = this.dialog.open(PolicyDialog, { data: this.policy });
	// 	dialogRef.afterClosed().subscribe((confirmed) => {
	// 		if (confirmed) {
	// 			this.caseStore.applyForPolicyWithCurrentUser(this.policy.id);
	// 		}
	// 	});
	}
}
