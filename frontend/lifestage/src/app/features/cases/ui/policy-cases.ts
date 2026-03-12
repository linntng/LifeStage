import { Component, inject, OnInit } from '@angular/core';
import { CaseStore } from '../state/case-store';

@Component({
	selector: 'app-policy-cases',
	imports: [],
	templateUrl: './policy-cases.html',
})
export class PolicyCases {
	policyCaseStore = inject(CaseStore);
	cases = this.policyCaseStore.cases;

	ngOnInit() {
		this.policyCaseStore.loadAllCases();
	}

}
