import { Component, inject } from '@angular/core';
import { LifeeventStore } from '../state/lifeevent-store';
import { CommonModule } from '@angular/common';
import { UserStore } from '../../user/state/user-store';
import { Lifeevent } from '../state/lifeevent-api';

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

	toggleEvent(event: Lifeevent) {
		if (this.userStore.currentUser()?.lifeEventIds.includes(event.id)) {
			this.userStore.removeLifeEventFromCurrentUser(event.id);
		} else {
			this.userStore.addLifeEventToCurrentUser(event.id);
		}
	}

	addMarriage() {
		this.userStore.addLifeEventToCurrentUser(286);
	}
}
