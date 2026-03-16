import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Auth } from '../../../core/auth/auth';
import { environment } from '../../../environments/environment';
import { PolicyCaseStatus } from './policy-case-status';

export interface Case {
	id: number;
	userId: string;
	policyId: number;
	status: PolicyCaseStatus;
}

export type CaseDTO = Omit<Case, 'id'>;

@Injectable({
	providedIn: 'root',
})
export class CaseApi {
	private http = inject(HttpClient);
	private auth = inject(Auth);

	private caseUrl = `${environment.caseApiUrl}/cases`;

	getUserPolicyCases(userId: string) {
		const headers = { Authorization: `Bearer ${this.auth.token()}` };
		return this.http.get<Case[]>(`${this.caseUrl}/user/${userId}`, { headers });
	}

	// Requires user to have role "CASE_HANDLER" or "ADMIN"
	getAllPolicyCases() {
		const headers = { Authorization: `Bearer ${this.auth.token()}` };
		return this.http.get<Case[]>(this.caseUrl, { headers });
	}

	addPolicyCaseToUser(userId: string, policyCase: CaseDTO) {
		const headers = {
			'Content-Type': 'application/json',
			Authorization: `Bearer ${this.auth.token()}`,
		};
		return this.http.post<Case>(`${this.caseUrl}/user/${userId}`, policyCase, { headers });
	}

	removePolicyCase(caseId: number) {
		const headers = { Authorization: `Bearer ${this.auth.token()}` };
		return this.http.delete(`${this.caseUrl}/${caseId}`, { headers });
	}

	updatePolicyCaseStatus(caseId: number, status: PolicyCaseStatus) {
		const headers = {
			'Content-Type': 'application/json',
			Authorization: `Bearer ${this.auth.token()}`,
		};
		return this.http.patch<Case>(`${this.caseUrl}/${caseId}/status`, { status }, { headers });
	}

}
