import { Component, computed, inject, OnInit } from '@angular/core';
import { CaseStore } from '../state/case-store';
import { PolicyCaseStatus } from '../state/policy-case-status';
import { CaseDialog } from './case-dialog/case-dialog';
import { MatDialog } from '@angular/material/dialog';
import { Case } from '../state/case-api';
import { UserStore } from '../../user/user-state/user-store';
import { User } from '../../user/user-state/user-api';
import { LifeeventStore } from '../../lifeevents/state/lifeevent-store';
import { PoliciesStore } from '../../policies/state/policies-store';

@Component({
	selector: 'app-policy-cases',
	imports: [],
	templateUrl: './policy-cases.html',
})
export class PolicyCases implements OnInit {
	policyCaseStore = inject(CaseStore);
	userStore = inject(UserStore);
	lifeEventStore = inject(LifeeventStore);
	policyStore = inject(PoliciesStore);
	cases = this.policyCaseStore.cases;
	users = this.userStore.users;
	activeCases = computed(() =>
		this.cases()?.filter((c) => c.status === PolicyCaseStatus.IN_REVIEW),
	);
	inactiveCases = computed(() =>
		this.cases()?.filter((c) => c.status !== PolicyCaseStatus.IN_REVIEW),
	);
	private dialog = inject(MatDialog);

	ngOnInit() {
		this.policyCaseStore.loadAllCases();
		this.userStore.loadAllUsers();
		this.lifeEventStore.loadLifeevents();
		this.policyStore.loadPolicies();
	}

	acceptCase(caseId: number) {
		this.policyCaseStore.updatePolicyCaseStatus(caseId, PolicyCaseStatus.ACCEPTED);
	}

	openDialog(policyCase: Case, user: User | undefined) {
		const dialogRef = this.dialog.open(CaseDialog, {
			data: {
				case: policyCase,
				user: user,
			},
		});
		dialogRef.afterClosed().subscribe((closeResult) => {
			if (closeResult === 'ACCEPTED') {
				this.acceptCase(policyCase.id);
			} else if (closeResult === 'DENIED') {
				this.policyCaseStore.updatePolicyCaseStatus(policyCase.id, PolicyCaseStatus.DENIED);
			}
		});
	}
}
