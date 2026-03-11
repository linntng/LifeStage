import { TestBed } from '@angular/core/testing';

import { PoliciesStore } from './policies-store';

describe('PoliciesStore', () => {
	let service: PoliciesStore;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(PoliciesStore);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
