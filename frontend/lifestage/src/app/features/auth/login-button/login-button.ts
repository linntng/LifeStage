import { Component, effect, inject } from '@angular/core';
import { Auth } from '../../../core/auth/auth';
import { UserStore } from '../../user/state/user-store';
import { KEYCLOAK_EVENT_SIGNAL, KeycloakEventType } from 'keycloak-angular';
import { User } from '../../user/ui/user';
import { CaseStore } from '../../cases/state/case-store';

@Component({
	selector: 'app-login-button',
	imports: [],
	templateUrl: './login-button.html',
})
export class LoginButton {
	private auth = inject(Auth);
	protected userStore = inject(UserStore);
	private readonly keycloakSignal = inject(KEYCLOAK_EVENT_SIGNAL);
	private caseStore = inject(CaseStore);
	authenticated = this.auth.authenticated;
	currentUser = this.userStore.currentUser;

	cases = this.caseStore.userCases;

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

	loadUserCases() {
		const currentUser = this.userStore.currentUser();
		if (currentUser) {
			this.caseStore.loadUserCases(currentUser.id);
		}
	}

	addTestCase() {
		const newCase = {
			id: 0,
			userId: '',
			policyId: 123,
			status: 'Workin baby!',
		};
		this.caseStore.addPolicyCaseToUser(newCase);
	}
}
