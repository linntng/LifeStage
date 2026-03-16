import { Component, computed, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserStore } from '../../user/state/user-store';
import { PoliciesStore } from '../state/policies-store';
import { InfoCard } from '../../../shared/info-card/info-card';
import { PolicyStatus } from '../state/policy-status.enum';
import { CaseStore } from '../../cases/state/case-store';
import { LifeeventStore } from '../../lifeevents/state/lifeevent-store';
import { CapitalizePipe } from '../../../shared/capitalize-pipe';
import { Policy } from '../state/policies-api';

@Component({
	selector: 'app-policy',
	imports: [InfoCard, CapitalizePipe, CommonModule],
	templateUrl: './policies.html',
})
export class Policies {
	userStore = inject(UserStore);
	policiesStore = inject(PoliciesStore);
	caseStore = inject(CaseStore);
	private lifeeventStore = inject(LifeeventStore);

	status(policy: Policy) {
		const user = this.userStore.currentUser();
		return user && policy && user.policyIds.includes(policy.id)
			? 'Active'
			: 'In review';
	}

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
		this.userStore
			.userPolicies()
			.filter((policy) => policy.status === PolicyStatus.ACTIVE)
			.concat(this.policiesInReview()),
	);
	remainingPolicies = computed(() => {
		const userPolicyIds = new Set(this.userStore.currentUser()?.policyIds || []);
		const policiesInReviewIds = new Set(this.policiesInReview()?.map((policy) => policy.id));
		const remainingPolicies = this.activePolicies()?.filter(
			(policy) => !policiesInReviewIds.has(policy.id) && !userPolicyIds.has(policy.id),
		);
		return remainingPolicies;
	});

	getLifeeventName(id: number) {
		return this.lifeeventStore.lifeevents()?.find((l) => l.id === id);
	}
}
