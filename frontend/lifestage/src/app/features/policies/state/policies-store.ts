import { inject, Injectable, signal } from '@angular/core';
import { Policy, PoliciesApi } from './policies-api';
import { PolicyStatus } from './policy-status.enum';
import { forkJoin } from 'rxjs';

@Injectable({
	providedIn: 'root',
})
export class PoliciesStore {
	private policiesApi = inject(PoliciesApi);
	readonly policies = signal<Policy[]>([]);
	readonly policiesToReview = signal<Policy[] | null>(null);
	readonly loading = signal(false);

	loadPolicies() {
		this.loading.set(true);

		forkJoin({
			review: this.policiesApi.getReviewPolicies(),
			all: this.policiesApi.getPolicies(),
		}).subscribe({
			next: ({ review, all }) => {
				this.policiesToReview.set(review);
				this.policies.set(all);
			},
			error: (e) => {
				console.error('Failed to fetch policies', e);
			},
			complete: () => {
				this.loading.set(false);
			},
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

	patchPolicy(policy: Policy) {
		this.policiesApi.patchPolicy(policy).subscribe({
			next: () => this.loadPolicies(),
			error: (e) => console.error('Failed to patch policy', e),
		});
	}
}
