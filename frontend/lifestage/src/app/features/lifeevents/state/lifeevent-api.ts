import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

export interface Lifeevent {
	id: number;
	name: string;
}

@Injectable({
	providedIn: 'root',
})
export class LifeeventApi {
	private http = inject(HttpClient);
	private lifeeventsUrl = `${environment.apiUrl}/lifeevents`;

	getLifeevents(): Observable<Lifeevent[]> {
		return this.http.get<Lifeevent[]>(this.lifeeventsUrl);
	}
}
