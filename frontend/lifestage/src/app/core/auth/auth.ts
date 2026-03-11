import { computed, inject, Injectable } from '@angular/core';
import Keycloak, { KeycloakUserInfo } from 'keycloak-js';
import { User } from '../../features/user/state/user-api';

@Injectable({
	providedIn: 'root',
})
export class Auth {
	private keycloak = inject(Keycloak);

	readonly authenticated = computed(() => this.keycloak.authenticated ?? false);
	readonly token = computed(() => this.keycloak.token);

	login() {
		this.keycloak.login();
	}

	logout() {
		this.keycloak.logout();
	}

	getUser() {
		return this.keycloak
			.loadUserInfo()
			.then((userInfo) => {
				if (!userInfo || !userInfo['sub']) {
					console.error("userInfo or userInfo['sub'] is null");
					return;
				}

				// Add user to db or find existing user
				const user = this.createUser(userInfo);
				return user;
			})
			.catch(() => {
				console.error("Couldn't get user info from Keycloak");
			});
	}

	private createUser(userInfo: KeycloakUserInfo) {
		const user: User = {
			id: userInfo!['sub'],
			username: userInfo!['preferred_username'],
			lifeEventIds: [],
			role: 'USER',
		};
		return user;
	}
}
