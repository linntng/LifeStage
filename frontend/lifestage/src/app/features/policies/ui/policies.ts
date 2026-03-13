import { Component, computed, inject } from '@angular/core';
import { UserStore } from '../../user/state/user-store';
import { PoliciesStore } from '../state/policies-store';
import { InfoCard } from './info-card/info-card';
import { PolicyStatus } from '../state/policy-status.enum';
import { CaseStore } from '../../cases/state/case-store';

@Component({
	selector: 'app-policy',
	imports: [InfoCard],
	templateUrl: './policies.html',
})
export class Policies {
	userStore = inject(UserStore);
	policiesStore = inject(PoliciesStore);
	caseStore = inject(CaseStore);

	activePolicies = computed(() =>
		this.policiesStore.policies().filter((policy) => policy.status === PolicyStatus.ACTIVE),
	);
	policiesInReview = computed(() => {
		const policyCases = this.caseStore.userCases();
		const policyIds = policyCases ? policyCases.map((policyCase) => policyCase.policyId) : [];
		const policies =
			policyIds.length > 0
				? this.policiesStore.policies().filter((policy) => policyIds.includes(policy.id))
				: [];
		return policies;
	});
	userPolicies = computed(() =>
		this.userStore.userPolicies().filter((policy) => policy.status === PolicyStatus.ACTIVE).concat(this.policiesInReview()),
	);
	remainingPolicies = computed(() => {
		const userPolicyIds = new Set(this.userStore.currentUser()?.policyIds || []);
		const policiesInReviewIds = new Set(this.policiesInReview()?.map((policy) => policy.id));
		const remainingPolicies = this.activePolicies()?.filter(
			(policy) => !policiesInReviewIds.has(policy.id) && !userPolicyIds.has(policy.id),
		);
		return remainingPolicies;
	});
}
