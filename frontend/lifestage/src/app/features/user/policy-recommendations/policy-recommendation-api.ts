import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Auth } from '../../../core/auth/auth';

export interface PolicyRecommendation {
	premiumImpact: string;
	userId: string;
	lifeEventId: number;
	policyEditActions: PolicyEditActions[];
}

export interface PolicyEditActions {
	policyId: number;
	action: 'ADD' | 'REMOVE';
}

@Injectable({
	providedIn: 'root',
})
export class PolicyRecommendationApi {
	private http = inject(HttpClient);
	private userUrl = `${environment.apiUrl}/users`;

	getPolicyRecommendationById(id: string) {
		return this.http.get<PolicyRecommendation>(`${this.userUrl}/${id}/policyrecommendation`);
	}
}
