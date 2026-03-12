import { Component, computed, inject } from '@angular/core';
import { UserStore } from '../../user/state/user-store';
import { PoliciesStore } from '../state/policies-store';
import { InfoCard } from './info-card/info-card';

@Component({
	selector: 'app-policy',
	imports: [InfoCard],
	templateUrl: './policies.html',
})
export class Policies {
	user = inject(UserStore);
	userPolicies = this.user.userPolicies;
	policiesStore = inject(PoliciesStore);
	policies = this.policiesStore.policies;

	remainingPolicies = computed(() => {
		const policyIds = new Set(this.user.currentUser()?.policyIds || []);
		return this.policiesStore.policies()?.filter((policy) => !policyIds.has(policy.id));
	});
}
