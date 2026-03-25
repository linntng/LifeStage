import { Component, inject } from '@angular/core';
import { Auth } from '../../../core/auth/auth';

@Component({
	selector: 'app-not-logged-in',
	imports: [],
	templateUrl: './not-logged-in.html',
})
export class NotLoggedIn {
	private auth = inject(Auth);

	login() {
		this.auth.login();
	}
}
