import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface PolicyAudit {
	id: string;
	rev: number;
	revtype: number;
	revtstmp: number;
}

export interface AccessAuditLog {
	id: number;
	username: string;
	action: string;
	resourceType: string;
	resourceId: string;
	createdAt: string;
}

@Injectable({
	providedIn: 'root',
})
export class AuditingState {
	private http = inject(HttpClient);
	private auditingUrl = `${environment.apiUrl}/internal/audit`;

	getPolicyAudits(): Observable<PolicyAudit[]> {
		return this.http.get<PolicyAudit[]>(`${this.auditingUrl}/policies`);
	}

	getAccessLogs(): Observable<AccessAuditLog[]> {
		return this.http.get<AccessAuditLog[]>(`${this.auditingUrl}/access-log`);
	}
}
