import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccessAuditLog, AuditingState, PolicyAudit } from './auditing-state';

@Component({
	selector: 'app-auditing-page',
	imports: [CommonModule],
	templateUrl: './auditing-page.html',
})
export class AuditingPage implements OnInit {
	private auditingService = inject(AuditingState);

	policyAudits: PolicyAudit[] = [];
	accessLogs: AccessAuditLog[] = [];

	loadingPolicies = true;
	loadingAccessLogs = true;

	policyError = '';
	accessError = '';

	ngOnInit(): void {
		this.loadPolicyAudits();
		this.loadAccessLogs();
	}

	loadPolicyAudits(): void {
		this.loadingPolicies = true;
		this.policyError = '';

		this.auditingService.getPolicyAudits().subscribe({
			next: (data) => {
				this.policyAudits = data;
				this.loadingPolicies = false;
			},
			error: (error) => {
				console.error('Failed to load policy audits', error);
				this.policyError = 'Could not load policy audit log.';
				this.loadingPolicies = false;
			},
		});
	}

	loadAccessLogs(): void {
		this.loadingAccessLogs = true;
		this.accessError = '';

		this.auditingService.getAccessLogs().subscribe({
			next: (data) => {
				this.accessLogs = data;
				this.loadingAccessLogs = false;
			},
			error: (error) => {
				console.error('Failed to load access logs', error);
				this.accessError = 'Could not load access audit log.';
				this.loadingAccessLogs = false;
			},
		});
	}

	getRevisionTypeLabel(type: number): string {
		switch (type) {
			case 0:
				return 'Created';
			case 1:
				return 'Updated';
			case 2:
				return 'Deleted';
			default:
				return 'Unknown';
		}
	}

	getRevisionTypeClass(type: number): string {
		switch (type) {
			case 0:
				return 'bg-green-100 text-green-800';
			case 1:
				return 'bg-yellow-100 text-yellow-800';
			case 2:
				return 'bg-red-100 text-red-800';
			default:
				return 'bg-gray-100 text-gray-800';
		}
	}
}
