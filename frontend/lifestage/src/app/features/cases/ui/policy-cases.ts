import { Component, computed, inject, OnInit } from '@angular/core';
import { CaseStore } from '../state/case-store';
import { PolicyCaseStatus } from '../state/policy-case-status';

@Component({
	selector: 'app-policy-cases',
	imports: [],
	templateUrl: './policy-cases.html',
})
export class PolicyCases {
	policyCaseStore = inject(CaseStore);
	cases = this.policyCaseStore.cases;
	activeCases = computed(() => this.cases()?.filter(c => c.status === PolicyCaseStatus.IN_REVIEW));
	inactiveCases = computed(() => this.cases()?.filter(c => c.status !== PolicyCaseStatus.IN_REVIEW));

	ngOnInit() {
		this.policyCaseStore.loadAllCases();
	}

	addCase() {
		this.policyCaseStore.addPolicyCaseToUser({
			userId: "test-user-id2",
			policyId: 0,
			status: PolicyCaseStatus.IN_REVIEW,
		});
	}

}
