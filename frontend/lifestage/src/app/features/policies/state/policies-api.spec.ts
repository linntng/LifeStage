import { TestBed } from '@angular/core/testing';

import { PoliciesApi } from './policies-api';

describe('PoliciesApi', () => {
	let service: PoliciesApi;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(PoliciesApi);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
