import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Auth } from '../../../core/auth/auth';

export interface User {
	id: string;
	username: string;
	lifeEventIds: number[]; // List of life event IDs associated with the user
  policyIds: number[];
	role: 'ADMIN' | 'USER' | 'CASE_HANDLER' | 'RISK_ANALYST' | null;

}

@Injectable({
	providedIn: 'root',
})
export class UserApi {
	private http = inject(HttpClient);
	private auth = inject(Auth);

	private userUrl = `${environment.apiUrl}/users`;

	private getAuthHeaders() {
		const token = this.auth.token();
		if (!token) {
			throw new Error('Missing access token for authenticated user request');
		}
		return {
			Authorization: `Bearer ${token}`,
		};
	}

	getUserById(id: string) {
		return this.http.get<User>(`${this.userUrl}/${id}`, {
			headers: this.getAuthHeaders(),
		});
	}

	addUser(user: User) {
		return this.http.post<User>(this.userUrl, user, {
			headers: this.getAuthHeaders(),
		});
	}

	addLifeEventToUser(userId: string, lifeEventId: number) {
		return this.http.post(`${this.userUrl}/${userId}/lifeevents`, lifeEventId, {
			headers: this.getAuthHeaders(),
		});
	}

	removeLifeEventFromUser(userId: string, lifeEventId: number) {
		return this.http.patch(`${this.userUrl}/${userId}/lifeevents/${lifeEventId}`, null, {
			headers: this.getAuthHeaders(),
		});
	}

	addPolicyToUser(userId: string, policyId: number) {
		return this.http.post(`${this.userUrl}/${userId}/policies`, policyId, {
      headers: this.getAuthHeaders()
     	});
    
	getAllUsers() {
		return this.http.get<User[]>(`${this.userUrl}`, {
			headers: this.getAuthHeaders(),
		});
	}

	removePolicyFromUser(userId: string, policyId: number) {
		return this.http.patch(`${this.userUrl}/${userId}/policies/${policyId}`, null, {
      headers: this.getAuthHeaders(),
      });
    
	changeRoleOfUser(userId: string, role: string) {
		return this.http.patch(`${this.userUrl}/${userId}/role`, role, {
			headers: this.getAuthHeaders(),
		});
	}
}
