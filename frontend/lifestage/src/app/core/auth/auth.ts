import { inject, Injectable, signal } from '@angular/core';
import Keycloak, { KeycloakUserInfo } from 'keycloak-js';
import { User } from '../../features/user/state/user-api';
import { UserStore } from '../../features/user/state/user-store';

@Injectable({
	providedIn: 'root',
})
export class Auth {
	private keycloak = inject(Keycloak);
	private userStore = inject(UserStore);

	readonly authenticated = signal(this.keycloak.authenticated);

	login() {
		this.keycloak.login();
	}

	logout() {
		this.keycloak.logout();
	}

	connectUser() {
		this.keycloak
			.loadUserInfo()
			.then((userInfo) => {
				if (!userInfo || !userInfo['sub']) {
					console.error("userInfo or userInfo['sub'] is null");
					return;
				}

				// Add user to db or find existing user
				const user = this.createUser(userInfo);
				this.userStore.loadOrCreateCurrentUser(user);
			})
			.catch(() => {
				console.error("Couldn't connect user to backend");
			});
	}

	private createUser(userInfo: KeycloakUserInfo) {
		const user: User = {
			id: userInfo!['sub'],
			username: userInfo!['preferred_username'],
			lifeEventIds: [],
		};
		return user;
	}
}
