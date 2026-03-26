import { inject, Injectable } from '@angular/core';
import { PolicyStatus } from './policy-status.enum';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

export interface Policy {
	id: number;
	name: string;
	coveredLifeEvents: number[];
	premium: number;
	status: PolicyStatus;
	inReview: boolean | null;
}

export interface PolicyRejection {
	rejectedPolicyId: number | null;
	explanation: string | null;
}

export type PolicyAudit = {
	policy: Policy;
	action: string;
	timestamp: string; // ISO string (from Instant)
};

@Injectable({
	providedIn: 'root',
})
export class PoliciesApi {
	http = inject(HttpClient);
	policiesUrl = `${environment.apiUrl}/policies`;

	getPolicies(): Observable<Policy[]> {
		return this.http.get<Policy[]>(this.policiesUrl);
	}

	postPolicy(policy: Policy): Observable<Policy> {
		return this.http.post<Policy>(this.policiesUrl, policy);
	}

	patchPolicy(policy: Policy, rejection: PolicyRejection): Observable<Policy> {
		return this.http.patch<Policy>(this.policiesUrl, { policy: policy, rejection: rejection });
	}

	getReviewPolicies() {
		return this.http.get<Policy[]>(`${this.policiesUrl}/review`);
	}

	getRejectedPolicies() {
		return this.http.get<PolicyRejection[]>(`${this.policiesUrl}/rejected`);
	}

	getPolicyAudit(): Observable<PolicyAudit[]> {
		return this.http.get<PolicyAudit[]>(`${this.policiesUrl}/audit`);
	}
}
