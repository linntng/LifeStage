import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Auth } from '../../../core/auth/auth';

export interface User {
	id: string;
	username: string;
	lifeEventIds: number[]; // List of life event IDs associated with the user
}

@Injectable({
	providedIn: 'root',
})
export class UserApi {
	private http = inject(HttpClient);
	private auth = inject(Auth);

	private userUrl = `${environment.apiUrl}/users`;

	getUserById(id: string) {
		return this.http.get<User>(`${this.userUrl}/${id}`);
	}

	addUser(user: User) {
		return this.http.post<User>(this.userUrl, user);
	}

	addLifeEventToUser(userId: string, lifeEventId: number) {
		const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${this.auth.token()}` };
		return this.http.post(`${this.userUrl}/${userId}/lifeevents/`, lifeEventId, { headers });
	}

	removeLifeEventFromUser(userId: string, lifeEventId: number) {
		const headers = { Authorization: `Bearer ${this.auth.token()}` };
		return this.http.delete(`${this.userUrl}/${userId}/lifeevents/${lifeEventId}`, { headers });
	}
}
