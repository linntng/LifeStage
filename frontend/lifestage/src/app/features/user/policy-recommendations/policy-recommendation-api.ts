import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { PolicyEditAction } from './policy-edit-action';

export interface PolicyRecommendation {
	id: number;
	premiumImpact: number;
	lifeEventId: number;
	userId: string;
	policyEditActions: PolicyEditAction[];
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
