import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

@Injectable({
	providedIn: 'root',
})
export class UserLifeeventApi {
	private http = inject(HttpClient);
	private userUrl = `${environment.apiUrl}/users`;

	addLifeEventToUser(userId: string, lifeEventId: number) {
		return this.http.post(`${this.userUrl}/${userId}/lifeevents`, lifeEventId);
	}

	removeLifeEventFromUser(userId: string, lifeEventId: number) {
		return this.http.patch(`${this.userUrl}/${userId}/lifeevents/${lifeEventId}`, null);
	}
}
