import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

export interface User {
	id: string;
	username: string;
	lifeEventIds: number[]; // List of life event IDs associated with the user
	policyIds: number[];
	role: UserRole | null;
}

export type UserRole = 'ADMIN' | 'USER' | 'CASE_HANDLER' | 'RISK_ANALYST' | 'POLICY_MANAGER';

@Injectable({
	providedIn: 'root',
})
export class UserApi {
	private http = inject(HttpClient);
	private userUrl = `${environment.apiUrl}/users`;

	getUserById(id: string) {
		return this.http.get<User>(`${this.userUrl}/${id}`);
	}

	addUser(user: User) {
		return this.http.post<User>(this.userUrl, user);
	}

	getAllUsers() {
		return this.http.get<User[]>(`${this.userUrl}`);
	}

	changeRoleOfUser(userId: string, role: string) {
		return this.http.patch(`${this.userUrl}/${userId}/role`, role);
	}
}
