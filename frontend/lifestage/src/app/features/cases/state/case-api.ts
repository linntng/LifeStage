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
		return this.http.get<Case[]>(`${this.caseUrl}/user/${userId}`);
	}

	// Requires user to have role "CASE_HANDLER" or "ADMIN"
	getAllPolicyCases() {
		return this.http.get<Case[]>(this.caseUrl);
	}

	addPolicyCaseToUser(userId: string, policyCase: CaseDTO) {
		const headers = {
			'Content-Type': 'application/json',
		};
		return this.http.post<Case>(`${this.caseUrl}/user/${userId}`, policyCase, { headers });
	}

	removePolicyCase(caseId: number) {
		return this.http.delete(`${this.caseUrl}/${caseId}`);
	}

	updatePolicyCaseStatus(caseId: number, status: PolicyCaseStatus) {
		const headers = {
			'Content-Type': 'application/json',
		};
		console.log('Updating policy case status', caseId, status);
		return this.http.patch<Case>(`${this.caseUrl}/${caseId}/status`, status, { headers });
	}
}
