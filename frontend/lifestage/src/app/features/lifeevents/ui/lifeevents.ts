import { Component, computed, inject, signal } from '@angular/core';
import { LifeeventStore } from '../state/lifeevent-store';
import { CommonModule } from '@angular/common';
import { UserStore } from '../../user/user-state/user-store';
import { Lifeevent } from '../state/lifeevent-api';
import { CapitalizePipe } from '../../../shared/capitalize-pipe';

@Component({
	selector: 'app-lifeevents',
	imports: [CommonModule, CapitalizePipe],
	templateUrl: './lifeevents.html',
})
export class Lifeevents {
	private lifeeventStore = inject(LifeeventStore);
	private userStore = inject(UserStore);

	lifeevents = this.lifeeventStore.lifeevents;
	userLifeevents = this.userStore.userLifeevents;
	selectedEventInfoBar = signal<number | null>(null);

	remainingLifeevents = computed(() => {
		const userEventIds = new Set(this.userStore.currentUser()?.lifeEventIds || []);
		return this.lifeeventStore.lifeevents().filter((event) => !userEventIds.has(event.id));
	});

	addEvent(event: Lifeevent) {
		this.userStore.addLifeEventToCurrentUser(event.id);
		console.log(this.userStore.currentUser());
	}

	removeEvent(event: Lifeevent) {
		this.userStore.removeLifeEventFromCurrentUser(event.id);
	}

	addMarriage() {
		this.userStore.addLifeEventToCurrentUser(286);
	}

	toggleEventInfoBar(id: number) {
		if (this.selectedEventInfoBar() === id) {
			this.selectedEventInfoBar.set(null);
		} else {
			this.selectedEventInfoBar.set(id);
		}
	}
}
