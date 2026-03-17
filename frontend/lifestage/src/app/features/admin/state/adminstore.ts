import { inject, Injectable, signal } from '@angular/core';
import { User, UserApi } from '../../user/user-state/user-api';

@Injectable({
	providedIn: 'root',
})
export class Adminstore {
	userApi = inject(UserApi);
	allUsers = signal<User[]>([]);

	getAllUsers() {
		this.userApi.getAllUsers().subscribe({
			next: (users) => {
				this.allUsers.set(users);
			},
			error: (err) => {
				console.error('Error fetching users', err);
			},
		});
	}

	changeRoleOfUser(user: User, role: string) {
		this.userApi.changeRoleOfUser(user.id, role).subscribe({
			next: () => this.getAllUsers(),
			error: (err) => console.error('Error changing role of user', err),
		});
	}
}
