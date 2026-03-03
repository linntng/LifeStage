import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

export type User = {
	id: string;
	username: string;
	lifeEvents: string[]; // List of life event IDs associated with the user
};

@Injectable({
	providedIn: 'root',
})
export class UserApi {
	private http = inject(HttpClient);
	private userUrl = `${environment.apiUrl}/user`;
	
	getUserById(id: string) {
		return this.http.get<User>(`${this.userUrl}/${id}`);
	}

	postUser(user: User) {
		return this.http.post<User>(this.userUrl, user);
	}
}
