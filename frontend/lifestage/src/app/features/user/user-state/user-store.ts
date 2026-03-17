import { computed, inject, Injectable, signal } from '@angular/core';
import { User, UserApi } from './user-api';
import { LifeeventStore } from '../../lifeevents/state/lifeevent-store';
import { Lifeevent } from '../../lifeevents/state/lifeevent-api';
import { PoliciesStore } from '../../policies/state/policies-store';
import { UserLifeeventApi } from '../user-lifeevent/user-lifeevent-api';
import { UserPolicyApi } from '../user-policy/user-policy-api';
import {
	PolicyRecommendation,
	PolicyRecommendationApi,
} from '../policy-recommendations/policy-recommendation-api';

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
	readonly userLifeevents = signal<Lifeevent[] | null>(null);
	readonly userPolicyRec = signal<PolicyRecommendation | null>(null);

	readonly userPolicies = computed(() => {
		const user = this.currentUser();
		const allPolicies = this.policiesStore.policies() ?? [];

		if (!user?.policyIds?.length) return [];

		const policyIds = new Set(user.policyIds);
		return allPolicies.filter((policy) => policyIds.has(policy.id));
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

	loadOrCreateCurrentUser(user: User) {
		this.currentUserLoading.set(true);
		this.userApi.getUserById(user.id).subscribe({
			next: (user) => {
				this.setCurrentUser(user);
				console.log(user);
				this.currentUserLoading.set(false);
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
			complete: () => {
				this.loadUserLifeevents();
			},
		});
	}

	clearCurrentUser() {
		this.setCurrentUser(null);
		this.userLifeevents.set(null);
	}

	private loadUserLifeevents() {
		this.userLifeevents.set(
			this.currentUser()
				?.lifeEventIds.map((eventId) => this.lifeEventStore.getLifeeventById(eventId))
				.filter((event): event is Lifeevent => event !== undefined) || [],
		);
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

				this.loadUserLifeevents();
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

					this.loadUserLifeevents();
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

		const addPolicies = recPolicies.filter((p) => p.action === 'ADD');

		return addPolicies.every((p) => userPolicyIds.has(p.policyId));
	});

	readonly policiesToAdd = computed(
		() => this.userPolicyRec()?.policyEditActions?.filter((a) => a.action === 'ADD') ?? [],
	);

	readonly policiesToRemove = computed(
		() => this.userPolicyRec()?.policyEditActions?.filter((a) => a.action === 'REMOVE') ?? [],
	);
}
