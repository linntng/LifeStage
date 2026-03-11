import { TestBed } from '@angular/core/testing';

import { CaseStore } from './case-store';

describe('CaseStore', () => {
	let service: CaseStore;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(CaseStore);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
