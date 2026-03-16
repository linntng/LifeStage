import { effect, inject, Injectable, signal } from '@angular/core';
import { Case, CaseApi, CaseDTO } from './case-api';
import { UserStore } from '../../user/state/user-store';
import { PolicyCaseStatus } from './policy-case-status';

@Injectable({
	providedIn: 'root',
})
export class CaseStore {
	private caseApi = inject(CaseApi);
	private userStore = inject(UserStore);

	readonly userCases = signal<Case[] | null>(null);
	readonly cases = signal<Case[] | null>(null);

	constructor() {
		effect(() => {
			if (!this.userStore.currentUserLoading()) {
				this.loadUserCases();
			}
		});
	}

	loadUserCases() {
		const userId = this.userStore.currentUser()?.id;
		if (!userId) {
			console.error('No current user found');
			return;
		}
		this.caseApi.getUserPolicyCases(userId).subscribe({
			next: (cases) => this.userCases.set(cases),
			error: (err) => console.error('Error loading user cases', err),
		});
	}

	addPolicyCaseToCurrentUser(policyCase: CaseDTO) {
		const currentUser = this.userStore.currentUser();
		if (!currentUser) {
			console.error('No current user found');
			return;
		}
		console.log('Adding policy case to user', currentUser.id, policyCase);
		this.caseApi.addPolicyCaseToUser(currentUser.id, policyCase).subscribe({
			next: (newCase) => {
				this.userCases.update((userCases) => [...(userCases || []), newCase]);
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

	applyForPolicyWithCurrentUser(policyId: number) {
		if (!this.userStore.currentUser()) {
			console.error('No current user found');
			return;
		}
		const caseDTO: CaseDTO = {
			policyId: policyId,
			userId: this.userStore.currentUser()?.id || '',
			status: PolicyCaseStatus.IN_REVIEW,
		};
		this.addPolicyCaseToCurrentUser(caseDTO);
	}

	removePolicyCaseFromCurrentUser(policyId: number) {
		const currentUser = this.userStore.currentUser();
		if (!currentUser) {
			console.error('No current user found');
			return;
		}
		const policyCase = this.userCases()?.find((c) => c.policyId === policyId);
		if (!policyCase) {
			console.error(
				'Policy case not found for user',
				currentUser.id,
				'with policy ID',
				policyId,
			);
			return;
		}
		this.caseApi.removePolicyCase(policyCase.id).subscribe({
			next: () => {
				this.userCases.update((userCases) =>
					userCases ? userCases.filter((c) => c.id !== policyCase.id) : null,
				);
			},
			error: (err) => {
				console.error('Error removing policy case', err);
			},
		});
	}

	updatePolicyCaseStatus(caseId: number, status: PolicyCaseStatus) {
		this.caseApi.updatePolicyCaseStatus(caseId, status).subscribe({
			next: (updatedCase) => {
				this.cases.update((cases) =>
					cases
						? cases.map((policyCase) => (policyCase.id === updatedCase.id ? updatedCase : policyCase))
						: null,
				);
				this.userCases.update((userCases) =>
					userCases
						? userCases.map((c) => (c.id === updatedCase.id ? updatedCase : c))
						: null,
				);
			},
			error: (err) => {
				console.error('Error updating policy case status', err);
			},
		});
	}
}
