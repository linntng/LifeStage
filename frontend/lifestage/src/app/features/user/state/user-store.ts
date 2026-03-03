import { inject, Injectable, signal } from '@angular/core';
import { User, UserApi } from './user-api';
import { LifeeventStore } from '../../lifeevents/state/lifeevent-store';
import { Lifeevent } from '../../lifeevents/state/lifeevent-api';

@Injectable({
	providedIn: 'root',
})
export class UserStore {
	private userApi = inject(UserApi);
	private lifeEventStore = inject(LifeeventStore);

	readonly currentUser = signal<User | null>(null);
	readonly currentUserLoading = signal(false);
	readonly userLifeEvents = signal<(Lifeevent | undefined)[] | null>(null);

	private setCurrentUser(user: User | null) {
		this.currentUser.set(user);
	}

	private addUser(user: User) {
		this.currentUserLoading.set(true);
		this.userApi.addUser(user).subscribe({
			next: (user) => {
				this.setCurrentUser(user);
			},
			error: (err) => {
				console.error('Error registering new user', err);
			},
			complete: () => {
				this.currentUserLoading.set(false);
			},
		});
	}

	loadOrCreateCurrentUser(user: User) {
		this.currentUserLoading.set(true);
		this.userApi.getUserById(user.id).subscribe({
			next: (user) => {
				this.setCurrentUser(user);
				this.currentUserLoading.set(false);
			},
			error: (err) => {
				if (err.status === 404) {
					console.log(`User not found, registering new user`);
					this.addUser(user);
					// currentUserLoading set to false in addUser
				} else {
					console.error('Error fetching user', err);
					this.currentUserLoading.set(false);
				}
			},
			complete: () => {
				this.loadUserLifeEvents();
			},
		});
	}

	clearCurrentUser() {
		this.setCurrentUser(null);
		this.userLifeEvents.set(null);
	}

	private loadUserLifeEvents() {
		this.userLifeEvents.set(
			this.currentUser()?.lifeEvents.map((eventId) =>
				this.lifeEventStore.getLifeeventById(eventId),
			) || [],
		);
	}
}
