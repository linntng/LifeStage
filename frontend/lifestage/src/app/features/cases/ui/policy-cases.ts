import { Component, computed, inject, OnInit } from '@angular/core';
import { CaseStore } from '../state/case-store';
import { PolicyCaseStatus } from '../state/policy-case-status';
import { CaseDialog } from './case-dialog/case-dialog';
import { MatDialog } from '@angular/material/dialog';
import { Case } from '../state/case-api';

@Component({
	selector: 'app-policy-cases',
	imports: [],
	templateUrl: './policy-cases.html',
})
export class PolicyCases implements OnInit {
	policyCaseStore = inject(CaseStore);
	cases = this.policyCaseStore.cases;
	activeCases = computed(() =>
		this.cases()?.filter((c) => c.status === PolicyCaseStatus.IN_REVIEW),
	);
	inactiveCases = computed(() =>
		this.cases()?.filter((c) => c.status !== PolicyCaseStatus.IN_REVIEW),
	);
	private dialog = inject(MatDialog);

	ngOnInit() {
		this.policyCaseStore.loadAllCases();
	}

	acceptCase(caseId: number) {
		this.policyCaseStore.updatePolicyCaseStatus(caseId, PolicyCaseStatus.ACCEPTED);
	}

	openDialog(policyCase: Case) {
		const dialogRef = this.dialog.open(CaseDialog, { data: policyCase });
		dialogRef.afterClosed().subscribe((closeResult) => {
			if (closeResult === 'ACCEPTED') {
				this.acceptCase(policyCase.id);
			} else if (closeResult === 'DENIED') {
				this.policyCaseStore.updatePolicyCaseStatus(policyCase.id, PolicyCaseStatus.DENIED);
			}
		});
	}
}
