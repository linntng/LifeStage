import { Component, computed, effect, inject } from '@angular/core';
import { UserStore } from '../../user/user-state/user-store';
import { CommonModule } from '@angular/common';
import { PoliciesStore } from '../state/policies-store';
import { InfoCard } from '../../../shared/info-card/info-card';
import { PolicyStatus } from '../state/policy-status.enum';
import { CaseStore } from '../../cases/state/case-store';
import { LifeeventStore } from '../../lifeevents/state/lifeevent-store';
import { CapitalizePipe } from '../../../shared/capitalize-pipe';
import { Policy } from '../state/policies-api';
import { Dialog } from '../../../shared/dialog/dialog';
import { MatDialog } from '@angular/material/dialog';
import { PolicyCaseStatus } from '../../cases/state/policy-case-status';

@Component({
	selector: 'app-policy',
	imports: [InfoCard, CapitalizePipe, CommonModule],
	templateUrl: './policies.html',
})
export class Policies {
	userStore = inject(UserStore);
	policiesStore = inject(PoliciesStore);
	caseStore = inject(CaseStore);
	private lifeeventStore = inject(LifeeventStore);
	private dialog = inject(MatDialog);
	user = this.userStore.currentUser;
	policiesToAdd = this.userStore.policiesToAdd;
	policiesToRemove = this.userStore.policiesToRemove;

	constructor() {
		effect(() => {
			const currentUser = this.user();

			if (currentUser) {
				this.userStore.loadPolicyRecommendations(currentUser.id);
			}
		});
	}

	status(policy: Policy) {
		const user = this.userStore.currentUser();
		const isActive = user?.policyIds.includes(policy.id);

		const shouldAdd = this.policiesToAdd().some((p) => p.policyId === policy.id);
		const shouldRemove = this.policiesToRemove().some((p) => p.policyId === policy.id);

		return {
			active: isActive,
			recommended: shouldAdd,
			remove: shouldRemove,
		};
	}

	activePolicies = computed(() =>
		this.policiesStore.policies().filter((policy) => policy.status === PolicyStatus.ACTIVE),
	);
	policiesInReview = computed(() => {
		const policyCases = this.caseStore.userCases()?.filter(
			(policyCase) =>
				policyCase.status === PolicyCaseStatus.IN_REVIEW,
		);
		const policyIds = policyCases ? policyCases.map((policyCase) => policyCase.policyId) : [];
		const policies =
			policyIds.length > 0
				? this.policiesStore.policies().filter((policy) => policyIds.includes(policy.id))
				: [];
		return policies;
	});
	userPolicies = computed(() =>
		this.userStore
			.userPolicies()
			.filter((policy) => policy.status === PolicyStatus.ACTIVE)
			.concat(this.policiesInReview()),
	);
	remainingPolicies = computed(() => {
		const userPolicyIds = new Set(this.userStore.currentUser()?.policyIds || []);
		const policiesInReviewIds = new Set(this.policiesInReview()?.map((policy) => policy.id));
		const remainingPolicies = this.activePolicies()?.filter(
			(policy) => !policiesInReviewIds.has(policy.id) && !userPolicyIds.has(policy.id),
		);
		return remainingPolicies;
	});

	getLifeeventName(id: number) {
		return this.lifeeventStore.lifeevents()?.find((l) => l.id === id);
	}

	openRemainingPolicyDialog(policy: Policy) {
		const dialogRef = this.dialog.open(Dialog, {
			data: {
				title: 'Request policy ' + policy.name,
				content:
					'Do you want to request ' +
					policy.name +
					'? Once requested, it will be sent for approval.',
			},
		});
		dialogRef.afterClosed().subscribe((confirmed) => {
			if (confirmed) {
				this.caseStore.applyForPolicyWithCurrentUser(policy.id);
			}
		});
	}
	openActivePolicyDialog(policy: Policy) {
		const dialogRef = this.dialog.open(Dialog, {
			data: {
				title: 'Cancel subscription for ' + policy.name,
				content:
					'Do you want to cancel your subscription to the ' + policy.name + ' policy?',
			},
		});
		dialogRef.afterClosed().subscribe((confirmed) => {
			if (confirmed) {
				if (this.policiesInReview().includes(policy)) {
					this.caseStore.removePolicyCaseFromCurrentUser(policy.id);
				} else {
					this.userStore.removePolicyFromCurrentUser(policy.id);
				}
			}
		});
	}
}
