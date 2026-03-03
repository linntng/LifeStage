import { inject, Injectable, signal } from '@angular/core';
import Keycloak from 'keycloak-js';

@Injectable({
	providedIn: 'root',
})
export class Auth {
	private keycloak = inject(Keycloak);

	login() {
		this.keycloak.login();
	}

	logout() {
		this.keycloak.logout();
	}

	readonly authenticated = signal(this.keycloak.authenticated);

}
