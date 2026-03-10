import { Component, inject, signal } from '@angular/core';
import { UserStore } from '../user/state/user-store';
import Keycloak from 'keycloak-js';
import { Auth } from '../../core/auth/auth';

@Component({
	selector: 'app-dashboard',
	imports: [],
	templateUrl: './dashboard.html',
})
export class Dashboard {
	userstore = inject(UserStore);
	user = this.userstore.currentUser;
	auth = inject(Auth);
	userLifeevents = this.userstore.userLifeevents;

	ngOnInit() {
		console.log(this.auth.token);
	}
}
