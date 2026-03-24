import { Component, computed, effect, inject, OnInit } from '@angular/core';
import { UserStore } from '../user/user-state/user-store';
import { Auth } from '../../core/auth/auth';
import { CapitalizePipe } from '../../shared/capitalize-pipe';
import { PoliciesStore } from '../policies/state/policies-store';
import { LifeeventStore } from '../lifeevents/state/lifeevent-store';
import { RouterLink, RouterModule } from '@angular/router';
import { NotLoggedIn } from './not-logged-in/not-logged-in';

@Component({
	selector: 'app-dashboard',
	imports: [CapitalizePipe, RouterLink, RouterModule, NotLoggedIn],
	templateUrl: './dashboard.html',
})
export class Dashboard implements OnInit {
	userstore = inject(UserStore);
	auth = inject(Auth);
	policiesStore = inject(PoliciesStore);
	lifeeventStore = inject(LifeeventStore);
	user = this.userstore.currentUser;
	userLifeevents = this.userstore.userLifeevents;
	userPolicies = this.userstore.userPolicies;
	allPolicies = this.policiesStore.policies;
	allLifeevents = this.lifeeventStore.lifeevents;
	userPolicyRec = this.userstore.userPolicyRec;
	alreadyCovered = this.userstore.alreadyCovered;
	policiesToAdd = this.userstore.policiesToAdd;
	policiesToRemove = this.userstore.policiesToRemove;

	constructor() {
		effect(() => {
			const currentUser = this.user();

			if (currentUser) {
				this.userstore.loadPolicyRecommendations(currentUser.id);
			}
		});
	}

	ngOnInit() {
		this.policiesStore.loadPolicies();
		this.lifeeventStore.loadLifeevents();
	}

	getPolicyName(policyId: number) {
		const policy = this.allPolicies()?.find((p) => p.id === policyId);
		return policy?.name ?? 'Unknown policy';
	}

	getLifeeventName(lifeeventId?: number) {
		const lifeevent = this.allLifeevents()?.find((l) => l.id === lifeeventId);
		return lifeevent?.name ?? 'Unknown lifeevent';
	}

	readonly isLoggedIn = computed(() => !!this.user());
}
