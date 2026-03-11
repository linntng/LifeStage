import { inject, Injectable, signal } from '@angular/core';
import { Case, CaseApi } from './case-api';
import { UserStore } from '../../user/state/user-store';

@Injectable({
	providedIn: 'root',
})
export class CaseStore {
	private caseApi = inject(CaseApi);
	private userStore = inject(UserStore)

	readonly userCases = signal<Case[] | null>(null);

	loadUserCases(userId: string) {
		this.caseApi.getUserPolicyCases(userId);
	}
	addPolicyCaseToUser(policyCase: Case) {
		const currentUser = this.userStore.currentUser();
		if (!currentUser) {
			console.error('No current user found');
			return;
		}
		this.caseApi.addPolicyCaseToUser(currentUser.id, policyCase).subscribe({
			next: (newCase) => {
				const currentCases = this.userCases() || [];
				this.userCases.set([...currentCases, newCase]);
			},
			error: (err) => {
				console.error('Error adding policy case to user', err);
			},
		});
	}
}
