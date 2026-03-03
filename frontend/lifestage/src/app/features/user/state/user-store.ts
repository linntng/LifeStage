import { inject, Injectable, signal } from '@angular/core';
import { User, UserApi } from './user-api';

@Injectable({
	providedIn: 'root',
})
export class UserStore {
	private userApi = inject(UserApi);
	readonly currentUser = signal<User | null>(null);
	readonly currentUserLoading = signal(false);

	private setCurrentUser(user: User | null) {
		this.currentUser.set(user);
	}

	private addUser(user : User) {
		this.currentUserLoading.set(true);
		this.userApi.postUser(user).subscribe({
			next: (user) => {
				this.setCurrentUser(user);
			},
			error: (err) => {
				console.error("Error registering new user", err);
			},
			complete: () => {
				this.currentUserLoading.set(false);
			}
		});
	}

	loadOrCreateCurrentUser(user : User) {
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
					console.error("Error fetching user", err);
					this.currentUserLoading.set(false);
				}
			},
		});
	}

	clearCurrentUser() {
		this.setCurrentUser(null);
	}

	
}
