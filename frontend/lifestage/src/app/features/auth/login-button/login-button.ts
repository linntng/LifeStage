import { Component, inject } from '@angular/core';
import { Auth } from '../../../core/auth/auth';
import { UserStore } from '../../user/state/user-store';

@Component({
	selector: 'app-login-button',
	imports: [],
	templateUrl: './login-button.html',
})
export class LoginButton {
	private auth = inject(Auth);
	protected userStore = inject(UserStore);

	authenticated = this.auth.authenticated;

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
