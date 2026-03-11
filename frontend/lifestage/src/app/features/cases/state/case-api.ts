import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Auth } from '../../../core/auth/auth';
import { environment } from '../../../environments/environment';


export interface Case {
	id: number;
	userId: string;
	policyId: number;
	status: string;
}

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

	addPolicyCaseToUser(userId: string, policyCase: Case) {
		const headers = { 'Content-Type': 'application/json', Authorization: `Bearer ${this.auth.token()}` };
		return this.http.post<Case>(`${this.caseUrl}/user/${userId}`, policyCase, { headers });
	}
}
