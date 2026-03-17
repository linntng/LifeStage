import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
	providedIn: 'root',
})
export class UserPolicyApi {
	private http = inject(HttpClient);
	private userUrl = `${environment.apiUrl}/users`;

	addPolicyToUser(userId: string, policyId: number) {
		return this.http.post(`${this.userUrl}/${userId}/policies`, policyId);
	}

	removePolicyFromUser(userId: string, policyId: number) {
		return this.http.patch(`${this.userUrl}/${userId}/policies/${policyId}`, null);
	}
}
