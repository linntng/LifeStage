import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

export type User = {
	id: string;
	username: string;
};

@Injectable({
	providedIn: 'root',
})
export class UserApi {
	private http = inject(HttpClient);
	private userUrl = '${environment.apiUrl}/user';
}
