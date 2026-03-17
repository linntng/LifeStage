import { TestBed } from '@angular/core/testing';

import { PolicyEditAction } from './policy-edit-action';

describe('PolicyEditAction', () => {
	let service: PolicyEditAction;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(PolicyEditAction);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
