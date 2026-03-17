import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, effect, ChangeDetectorRef } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserStore } from '../user/state/user-store';
import { User } from '../user/state/user-api';

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
	styleUrls: ['./admin.css'],
})
export class Admin implements OnInit {
	userStore = inject(UserStore);
	cdr = inject(ChangeDetectorRef);

	searchTerm = '';
	filteredUsers: User[] = [];
	editingUser: User | null = null;
	userRoles: string[] = Object.values(UserRoles);
	selectedRole: string | null = null;

	constructor() {
		effect(() => {
			this.filteredUsers = this.userStore.allUsers();
			this.cdr.detectChanges(); // force UI update
		});
	}

	ngOnInit(): void {
		this.userStore.getAllUsers();
	}

	filterUsers() {
		const term = this.searchTerm.toLowerCase();

		this.filteredUsers = this.userStore
			.allUsers()
			.filter(
				(user) =>
					user.username.toLowerCase().includes(term) ||
					user.id.toLowerCase().includes(term) ||
					user.role?.toLowerCase().includes(term),
			);
	}

	changeRole() {
		this.userStore.changeRoleOfUser(this.editingUser!, this.selectedRole!);
	}
}
