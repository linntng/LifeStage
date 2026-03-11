import { inject, Injectable } from '@angular/core';
import { PolicyStatus } from './policy-status.enum';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

export interface Policy {
	id: number;
	name: string;
	premium: number;
	status: PolicyStatus;
}

@Injectable({
	providedIn: 'root',
})
export class PoliciesApi {
	http = inject(HttpClient);
	policiesUrl = `${environment.apiUrl}/policies`;

	getPolicies(): Observable<Policy[]> {
		return this.http.get<Policy[]>(this.policiesUrl);
	}
}
