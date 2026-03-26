import { computed, inject, Injectable, signal } from '@angular/core';
import { Policy, PoliciesApi, PolicyRejection, PolicyAudit } from './policies-api';
import { PolicyStatus } from './policy-status.enum';

@Injectable({
	providedIn: 'root',
})
export class PoliciesStore {
	private policiesApi = inject(PoliciesApi);
	readonly policies = signal<Policy[]>([]);
	readonly policiesToReview = signal<Policy[] | null>(null);
	readonly policiesRejected = signal<PolicyRejection[] | null>(null);
	readonly policyAudit = signal<PolicyAudit[] | null>(null);
	readonly getPolicyAuditLoading = signal(false);
	readonly getAllPoliciesLoading = signal(false);
	readonly getReviewPoliciesLoading = signal(false);
	readonly getRejectedPoliciesLoading = signal(false);
	readonly loading = computed(
		() =>
			this.getAllPoliciesLoading() ||
			this.getReviewPoliciesLoading() ||
			this.getRejectedPoliciesLoading() ||
			this.getPolicyAuditLoading(),
	);

	loadPolicies() {
		if (this.loading()) return;

		this.getAllPoliciesLoading.set(true);
		this.getReviewPoliciesLoading.set(true);
		this.getRejectedPoliciesLoading.set(true);
		this.getPolicyAuditLoading.set(true);

		this.policiesApi.getReviewPolicies().subscribe({
			next: (reviewPolicies) => this.policiesToReview.set(reviewPolicies),
			error: (err) => console.error('Failed to fetch policies for review', err),
			complete: () => this.getReviewPoliciesLoading.set(false),
		});

		this.policiesApi.getRejectedPolicies().subscribe({
			next: (rejectedPolicies) => this.policiesRejected.set(rejectedPolicies),
			error: (err) => console.error('Failed to fetch rejected policies', err),
			complete: () => this.getRejectedPoliciesLoading.set(false),
		});

		this.policiesApi.getPolicies().subscribe({
			next: (policies) => this.policies.set(policies),
			error: (err) => console.error('Failed to fetch policies', err),
			complete: () => this.getAllPoliciesLoading.set(false),
		});

		this.policiesApi.getPolicyAudit().subscribe({
			next: (audit) => this.policyAudit.set(audit),
			error: (err) => console.error('Failed to fetch policy audit', err),
			complete: () => this.getPolicyAuditLoading.set(false),
		});
	}

	getPolicyById(id: number) {
		return this.policies().find((policy: Policy) => policy.id === id);
	}

	getPoliciesFilteredByStatus(status: PolicyStatus[]) {
		return this.policies().filter((p) => status.includes(p.status));
	}

	postPolicy(policy: Policy) {
		this.policiesApi.postPolicy(policy).subscribe({
			next: () => this.loadPolicies(),
			error: () => console.error('Failed to post new policy'),
		});
	}

	patchPolicy(policy: Policy, rejection: PolicyRejection) {
		this.policiesApi.patchPolicy(policy, rejection).subscribe({
			next: () => this.loadPolicies(),
			error: (e) => console.error('Failed to patch policy', e),
		});
	}
}
