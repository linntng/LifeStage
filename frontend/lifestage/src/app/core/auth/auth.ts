import { computed, effect, inject, Injectable } from '@angular/core';
import Keycloak, { KeycloakUserInfo } from 'keycloak-js';
import { UserStore } from '../../features/user/state/user-store';
import { User } from '../../features/user/state/user-api';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';

@Injectable({
	providedIn: 'root',
})
export class Auth {
	private keycloak = inject(Keycloak);
	private userStore = inject(UserStore);
	private readonly keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);

	readonly authenticated = computed(() => this.keycloak.authenticated ?? false);

	constructor() {
		effect(() => {
			const keycloakEvent = this.keycloakSignal();

			if (keycloakEvent.type === KeycloakEventType.Ready) {
				this.connectUser();
			}
		});
	}

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
