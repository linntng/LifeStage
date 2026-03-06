import { Component, effect, inject } from '@angular/core';
import { Auth } from '../../../core/auth/auth';
import { UserStore } from '../../user/state/user-store';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';
import { User } from '../../user/ui/user';

@Component({
	selector: 'app-login-button',
	imports: [],
	templateUrl: './login-button.html',
})
export class LoginButton {
	private auth = inject(Auth);
	protected userStore = inject(UserStore);
	private readonly keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);

	authenticated = this.auth.authenticated;

	constructor() {
		effect(() => {
			const keycloakEvent = this.keycloakSignal();

			if (keycloakEvent.type === KeycloakEventType.Ready) {
				this.connectUser();
			}
		});
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

	connectUser() {
		this.auth.getUser().then((user) => {
			if (user) {
				this.userStore.loadOrCreateCurrentUser(user);
			}
		});
	}
}
