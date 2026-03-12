import { inject, Injectable, signal } from '@angular/core';
import { Policy, PoliciesApi } from './policies-api';

@Injectable({
	providedIn: 'root',
})
export class PoliciesStore {
	private policiesApi = inject(PoliciesApi);
	readonly policies = signal<Policy[]>([]);
	readonly loading = signal(false);

	loadPolicies() {
		this.loading.set(true);
		this.policiesApi.getPolicies().subscribe({
			next: (policies) => {
				this.policies.set(policies);
			},
			error: () => {
				console.error('Failed to fetch policies');
			},
			complete: () => {
				this.loading.set(false);
			},
		});
	}

	getPolicyById(id: number) {
		return this.policies().find((policy: Policy) => policy.id === id);
	}
}
