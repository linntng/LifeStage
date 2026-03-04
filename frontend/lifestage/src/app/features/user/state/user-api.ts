import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

export interface User {
	id: string;
	username: string;
	lifeEvents: number[]; // List of life event IDs associated with the user
}

@Injectable({
	providedIn: 'root',
})
export class UserApi {
	private http = inject(HttpClient);
	private userUrl = `${environment.apiUrl}/user`;

	getUserById(id: string) {
		return this.http.get<User>(`${this.userUrl}/${id}`);
	}

	addUser(user: User) {
		return this.http.post<User>(this.userUrl, user);
	}

	addLifeEventToUser(userId: string, lifeEventId: number) {
		const headers = { 'Content-Type': 'application/json' };
		return this.http.post(
			`${this.userUrl}/${userId}/lifeevents/`,
			{ id: lifeEventId },
			{ headers },
		);
	}

	removeLifeEventFromUser(userId: string, lifeEventId: number) {
		return this.http.delete(`${this.userUrl}/${userId}/lifeevents/${lifeEventId}`);
	}
}
