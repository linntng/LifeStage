import { Component, inject } from '@angular/core';
import { UserStore } from '../user/state/user-store';
import { Auth } from '../../core/auth/auth';
import { CapitalizePipe } from '../../shared/capitalize-pipe';

@Component({
	selector: 'app-dashboard',
	imports: [CapitalizePipe],
	templateUrl: './dashboard.html',
})
export class Dashboard {
	userstore = inject(UserStore);
	user = this.userstore.currentUser;
	auth = inject(Auth);
	userLifeevents = this.userstore.userLifeevents;
	userPolicies = this.userstore.userPolicies;
}
