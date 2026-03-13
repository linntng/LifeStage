import { Component, inject, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Policy } from '../../state/policies-api';
import { LifeeventStore } from '../../../lifeevents/state/lifeevent-store';
import { MatDialog } from '@angular/material/dialog';
import { PolicyDialog } from '../policy-dialog/policy-dialog';
import { CapitalizePipe } from '../../../../shared/capitalize-pipe';
import { UserStore } from '../../../user/state/user-store';
import { CaseStore } from '../../../cases/state/case-store';

@Component({
	selector: 'app-info-card',
	imports: [MatCardModule, CapitalizePipe],
	templateUrl: './info-card.html',
})
export class InfoCard {
	@Input() policy!: Policy;
	@Input() showStatus = false;

	private dialog = inject(MatDialog);
	private lifeeventStore = inject(LifeeventStore);
	private userStore = inject(UserStore);
	private caseStore = inject(CaseStore);
	currentUser = this.userStore.currentUser;
	get status() {
		const user = this.currentUser();
		return user && this.policy && user.policyIds.includes(this.policy.id) ? "Active" : "In review";
	}

	getLifeeventName(id: number) {
		return this.lifeeventStore.lifeevents()?.find((l) => l.id === id);
	}

	openDialog() {
		const dialogRef = this.dialog.open(PolicyDialog, { data: this.policy });
		dialogRef.afterClosed().subscribe((confirmed) => {
			if (confirmed) {
				this.caseStore.applyForPolicyWithCurrentUser(this.policy.id);
			}
		});
	}
}
