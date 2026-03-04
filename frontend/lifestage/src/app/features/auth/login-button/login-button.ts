import { Component, inject } from '@angular/core';
import { Auth } from '../../../core/auth/auth';

@Component({
	selector: 'app-login-button',
	imports: [],
	templateUrl: './login-button.html',
})
export class LoginButton {
	private auth = inject(Auth);

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
		this.authenticated() ? this.logout() : this.login();
	}
}
