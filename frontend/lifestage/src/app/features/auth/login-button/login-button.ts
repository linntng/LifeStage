import { Component, inject, effect } from '@angular/core';
import { Auth } from '../../../core/auth/auth';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';

@Component({
	selector: 'app-login-button',
	imports: [],
	templateUrl: './login-button.html',
})
export class LoginButton {
	private auth = inject(Auth);
	private readonly keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);

	constructor() {
		effect(() => {
			const keycloakEvent = this.keycloakSignal();

			if (keycloakEvent.type === KeycloakEventType.Ready) {
				this.auth.connectUser();
			}
		});
	}

	authenticated() {
		return this.auth.authenticated();
	}

	login() {
		this.auth.login();
	}

	logout() {
		this.auth.logout();
	}

	toggleAuth() {
		if (this.authenticated()) {
			this.logout();
		} else {
			this.login();
		}
	}
}
