import { computed, effect, inject, Injectable, signal } from '@angular/core';
import Keycloak, { KeycloakUserInfo } from 'keycloak-js';
import { User } from '../../features/user/state/user-api';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';

@Injectable({
	providedIn: 'root',
})
export class Auth {
	protected readonly keycloak = inject(Keycloak);

	readonly authenticated = computed(() => this.keycloak.authenticated ?? false);
	readonly token = computed(() => this.keycloak.token);
	keycloakName = signal('');

	private readonly keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);

	constructor() {
		this.getGivenName();

		effect(() => {
			this.keycloakSignal();
		});
	}

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

	getGivenName() {
		this.keycloak
			.loadUserInfo()
			.then((userInfo) => {
				console.log(userInfo);
				if (userInfo['given_name']) {
					this.keycloakName.set(userInfo['given_name']);
				} else {
					this.keycloakName.set('');
				}
			})
			.catch(() => {
				this.keycloakName.set('');
			});
	}

	private createUser(userInfo: KeycloakUserInfo) {
		const user: User = {
			id: userInfo!['sub'],
			username: userInfo!['preferred_username'],
			lifeEventIds: [],
			policyIds: [],
			role: 'USER',
		};
		return user;
	}
}
