import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, effect, ChangeDetectorRef, computed } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../user/user-state/user-api';
import { Adminstore } from './state/adminstore';
import { PoliciesStore } from '../policies/state/policies-store';

export const UserRoles = {
	ADMIN: 'ADMIN',
	USER: 'USER',
	CASE_HANDLER: 'CASE_HANDLER',
	RISK_ANALYST: 'RISK_ANALYST',
	POLICY_MANAGER: 'POLICY_MANAGER',
} as const;

export type UserRoles = (typeof UserRoles)[keyof typeof UserRoles];

@Component({
	selector: 'app-admin',
	imports: [CommonModule, FormsModule],
	templateUrl: './admin.html',
})
export class Admin implements OnInit {
	policiesStore = inject(PoliciesStore);
	adminStore = inject(Adminstore);
	cdr = inject(ChangeDetectorRef);
	policyAudit = this.policiesStore.policyAudit;
	auditLogs = computed(() => {
		const logs = this.policiesStore.policyAudit() ?? [];

		return [...logs].sort(
			(a, b) => new Date(b.timestamp).getTime() - new Date(a.timestamp).getTime(),
		);
	});

	searchTerm = '';
	filteredUsers: User[] = [];
	editingUser: User | null = null;
	userRoles: string[] = Object.values(UserRoles);
	selectedRole: string | null = null;

	activePage: 'users' | 'audit' = 'users';
	auditTab: 'policy' | 'policyCase' | 'user' = 'policy';

	constructor() {
		effect(() => {
			this.filteredUsers = this.adminStore.allUsers();
			this.cdr.detectChanges(); // force UI update
		});
	}

	ngOnInit(): void {
		this.adminStore.getAllUsers();
		this.policiesStore.loadPolicies();
	}

	filterUsers() {
		const term = this.searchTerm.toLowerCase();

		this.filteredUsers = this.adminStore
			.allUsers()
			.filter(
				(user) =>
					user.username.toLowerCase().includes(term) ||
					user.id.toLowerCase().includes(term) ||
					user.role?.toLowerCase().includes(term),
			);
	}

	changeRole() {
		this.adminStore.changeRoleOfUser(this.editingUser!, this.selectedRole!);
	}
}
