import { Component, inject, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Policy } from '../../state/policies-api';
import { LifeeventStore } from '../../../lifeevents/state/lifeevent-store';
import { MatDialog } from '@angular/material/dialog';
import { PolicyDialog } from '../policy-dialog/policy-dialog';
import { CapitalizePipe } from '../../../../shared/capitalize-pipe';

@Component({
	selector: 'app-info-card',
	imports: [MatCardModule, CapitalizePipe],
	templateUrl: './info-card.html',
})
export class InfoCard {
	@Input() policy!: Policy;

	private dialog = inject(MatDialog);

	lifeeventStore = inject(LifeeventStore);

	getLifeeventName(id: number) {
		return this.lifeeventStore.lifeevents()?.find((l) => l.id === id);
	}

	openDialog() {
		const dialogRef = this.dialog.open(PolicyDialog, { data: this.policy });

		dialogRef.afterClosed().subscribe((confirmed) => {
			if (confirmed) {
				console.log('Nice :)');
			}
		});
	}
}
