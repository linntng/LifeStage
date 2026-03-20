import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, effect, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { User } from '../user/user-state/user-api';
import { Adminstore } from './state/adminstore';

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
	adminStore = inject(Adminstore);
	cdr = inject(ChangeDetectorRef);

	searchTerm = '';
	filteredUsers: User[] = [];
	editingUser: User | null = null;
	userRoles: string[] = Object.values(UserRoles);
	selectedRole: string | null = null;

	constructor() {
		effect(() => {
			this.filteredUsers = this.adminStore.allUsers();
			this.cdr.detectChanges(); // force UI update
		});
	}

	ngOnInit(): void {
		this.adminStore.getAllUsers();
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
