import { computed, inject, Injectable, signal } from '@angular/core';
import { User, UserApi } from './user-api';
import { LifeeventStore } from '../../lifeevents/state/lifeevent-store';
import { PoliciesStore } from '../../policies/state/policies-store';
import { UserLifeeventApi } from '../user-lifeevent/user-lifeevent-api';
import { UserPolicyApi } from '../user-policy/user-policy-api';
import {
	PolicyRecommendation,
	PolicyRecommendationApi,
} from '../policy-recommendations/policy-recommendation-api';
import { Router } from '@angular/router';

@Injectable({
	providedIn: 'root',
})
export class UserStore {
	private userApi = inject(UserApi);
	private userLifeeventApi = inject(UserLifeeventApi);
	private userPolicyApi = inject(UserPolicyApi);
	private userPolicyRecApi = inject(PolicyRecommendationApi);
	private lifeEventStore = inject(LifeeventStore);
	private policiesStore = inject(PoliciesStore);

	readonly currentUser = signal<User | null>(null);
	readonly currentUserLoading = signal(false);
	readonly userPolicyRec = signal<PolicyRecommendation | null>(null);
	readonly users = signal<User[]>([]);
	router = inject(Router);

	readonly userPolicies = computed(() => {
		const user = this.currentUser();
		const allPolicies = this.policiesStore.policies() ?? [];

		if (!user?.policyIds?.length) return [];

		const policyIds = new Set(user.policyIds);
		return allPolicies.filter((policy) => policyIds.has(policy.id));
	});

	userLifeevents = computed(() => {
		const user = this.currentUser();
		const allLifeevents = this.lifeEventStore.lifeevents() ?? [];

		if (!user?.lifeEventIds?.length) return [];

		const lifeeventIds = new Set(user.lifeEventIds);
		return allLifeevents.filter((lifeevent) => lifeeventIds.has(lifeevent.id));
	});

	private setCurrentUser(user: User | null) {
		this.currentUser.set(user);
	}

	private addUser(user: User) {
		this.currentUserLoading.set(true);
		this.userApi.addUser(user).subscribe({
			next: (user) => {
				this.setCurrentUser(user);
			},
			error: (err) => {
				console.error('Error registering new user', err);
			},
			complete: () => {
				this.currentUserLoading.set(false);
			},
		});
	}

	loadAllUsers() {
		this.userApi.getAllUsers().subscribe({
			next: (users) => this.users.set(users),
			error: (err) => console.error('Error loading all users', err),
		});
	}

	loadOrCreateCurrentUser(user: User) {
		this.currentUserLoading.set(true);
		this.userApi.getUserById(user.id).subscribe({
			next: (user) => {
				this.setCurrentUser(user);
				this.currentUserLoading.set(false);
				if (user.role === 'CASE_HANDLER') {
					this.router.navigate(['/cases']);
				} else if (user.role === 'ADMIN') {
					this.router.navigate(['/admin']);
				} else if (user.role === 'POLICY_MANAGER') {
					this.router.navigate(['/policy-manager']);
				} else {
					this.router.navigate(['/']);
				}
			},
			error: (err) => {
				if (err.status === 404) {
					console.log(`User not found, registering new user`);
					this.addUser(user);
					// currentUserLoading set to false in addUser
				} else {
					console.error('Error fetching user', err);
					this.currentUserLoading.set(false);
				}
			},
		});
	}

	clearCurrentUser() {
		this.setCurrentUser(null);
	}

	addLifeEventToCurrentUser(lifeEventId: number) {
		if (!this.currentUser()) {
			console.error('No current user to add life event to');
			return;
		}
		this.userLifeeventApi.addLifeEventToUser(this.currentUser()!.id, lifeEventId).subscribe({
			next: () => {
				// Add life event ID to current user signal
				this.currentUser.update((user) => {
					if (!user) {
						console.error('No current user');
						return null;
					}
					return {
						...user,
						lifeEventIds: [...user.lifeEventIds, lifeEventId],
					};
				});
			},
			error: (err) => {
				console.error('Error adding life event to user', err);
			},
		});
	}

	removeLifeEventFromCurrentUser(lifeEventId: number) {
		if (!this.currentUser()) {
			console.error('No current user to remove life event from');
			return;
		}
		this.userLifeeventApi
			.removeLifeEventFromUser(this.currentUser()!.id, lifeEventId)
			.subscribe({
				next: () => {
					// Remove the life event ID from the user signal
					this.currentUser.update((user) => {
						if (!user) {
							console.error('No current user');
							return null;
						}
						return {
							...user,
							lifeEventIds: user.lifeEventIds.filter((id) => id !== lifeEventId),
						};
					});
				},
				error: (err) => {
					console.error('Error removing life event from user', err);
				},
			});
	}

	addPolicyToCurrentUser(policyId: number) {
		if (!this.currentUser()) {
			console.error('No current user to add policy to');
			return;
		}
		this.userPolicyApi.addPolicyToUser(this.currentUser()!.id, policyId).subscribe({
			next: () => {
				// Add life event ID to current user signal
				this.currentUser.update((user) => {
					if (!user) {
						console.error('No current user');
						return null;
					}
					return {
						...user,
						policyIds: [...user.policyIds, policyId],
					};
				});
			},
			error: (err) => {
				console.error('Error adding policy to user', err);
			},
		});
	}

	removePolicyFromCurrentUser(policyId: number) {
		if (!this.currentUser()) {
			console.error('No current user to remove policy from');
			return;
		}
		this.userPolicyApi.removePolicyFromUser(this.currentUser()!.id, policyId).subscribe({
			next: () => {
				// Remove the life event ID from the user signal
				this.currentUser.update((user) => {
					if (!user) {
						console.error('No current user');
						return null;
					}
					return {
						...user,
						policyIds: user.policyIds.filter((id) => id !== policyId),
					};
				});
			},
			error: (err) => {
				console.error('Error removing policy from user', err);
			},
		});
	}

	loadPolicyRecommendations(userId: string) {
		this.userPolicyRecApi.getPolicyRecommendationById(userId).subscribe({
			next: (data) => {
				this.userPolicyRec.set(data);
			},
			error: (err) => console.error('Error loading policy recommendations for user', err),
		});
	}

	readonly alreadyCovered = computed(() => {
		const recPolicies = this.userPolicyRec()?.policyEditActions ?? [];
		const userPolicies = this.userPolicies() ?? [];

		const userPolicyIds = new Set(userPolicies.map((p) => p.id));

		const shouldAddPolicies = recPolicies.filter((p) => p.action === 'ADD');
		const shouldRemovePolicies = recPolicies.filter((p) => p.action === 'REMOVE');

		const allAddsCovered = shouldAddPolicies.every((p) => userPolicyIds.has(p.policyId));
		const noRemovesPresent = shouldRemovePolicies.every((p) => !userPolicyIds.has(p.policyId));

		return allAddsCovered && noRemovesPresent;
	});

	readonly policiesToAdd = computed(
		() => this.userPolicyRec()?.policyEditActions?.filter((a) => a.action === 'ADD') ?? [],
	);

	readonly policiesToRemove = computed(
		() => this.userPolicyRec()?.policyEditActions?.filter((a) => a.action === 'REMOVE') ?? [],
	);
	/*
	redirectUser(router: Router) {
		const user = this.currentUser();
		if (!user) return;

		if (user.role === 'ADMIN') {
			router.navigate(['/admin']);
		} else if (user.role === 'CASE_HANDLER') {
			router.navigate(['/policy-cases']);
		} else {
			router.navigate(['/']);
		}
	}
		*/
}
