import { inject, Injectable, signal } from '@angular/core';
import { Case, CaseApi, CaseDTO } from './case-api';
import { UserStore } from '../../user/user-state/user-store';

@Injectable({
	providedIn: 'root',
})
export class CaseStore {
	private caseApi = inject(CaseApi);
	private userStore = inject(UserStore);

	readonly userCases = signal<Case[] | null>(null);
	readonly cases = signal<Case[] | null>(null);

	loadUserCases(userId: string) {
		this.caseApi.getUserPolicyCases(userId).subscribe({
			next: (cases) => this.userCases.set(cases),
			error: (err) => console.error('Error loading user cases', err),
		});
	}
	addPolicyCaseToUser(policyCase: CaseDTO) {
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

	loadAllCases() {
		this.caseApi.getAllPolicyCases().subscribe({
			next: (cases) => this.cases.set(cases),
			error: (err) => console.error('Error loading all cases', err),
		});
	}
}
