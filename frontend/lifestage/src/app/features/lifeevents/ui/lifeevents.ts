import { Component, inject } from '@angular/core';
import { LifeeventStore } from '../state/lifeevent-store';
import { CommonModule } from '@angular/common';
import { UserStore } from '../../user/state/user-store';

@Component({
	selector: 'app-lifeevents',
	imports: [CommonModule],
	templateUrl: './lifeevents.html',
})
export class Lifeevents {
	private lifeeventStore = inject(LifeeventStore);
	private userStore = inject(UserStore);

	lifeevents = this.lifeeventStore.lifeevents;
	userLifeevents = this.userStore.userLifeevents;
}
