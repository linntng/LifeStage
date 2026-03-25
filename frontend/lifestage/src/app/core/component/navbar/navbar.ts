import { Component, computed, inject } from '@angular/core';
import { RouterModule } from '@angular/router';
import { LoginButton } from '../../../features/auth/login-button/login-button';
import { CommonModule } from '@angular/common';
import { UserStore } from '../../../features/user/user-state/user-store';

@Component({
	selector: 'app-navbar',
	imports: [RouterModule, LoginButton, CommonModule],
	templateUrl: './navbar.html',
})
export class Navbar {
	menuOpen = false;
	userStore = inject(UserStore);

	readonly homeRoute = computed(() => {
		const role = this.userStore.currentUser()?.role;

		switch (role) {
			case 'CASE_HANDLER':
				return '/cases';
			case 'ADMIN':
				return '/admin';
			case 'POLICY_MANAGER':
				return '/policy-manager';
			case 'USER':
				return '/';
			default:
				return '/';
		}
	});
}
