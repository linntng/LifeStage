import { TestBed } from '@angular/core/testing';

import { CaseApi } from './case-api';

describe('CaseApi', () => {
	let service: CaseApi;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(CaseApi);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
