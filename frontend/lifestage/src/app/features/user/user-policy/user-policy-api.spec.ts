import { TestBed } from '@angular/core/testing';

import { UserPolicyApi } from './user-policy-api';

describe('UserPolicyApi', () => {
	let service: UserPolicyApi;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(UserPolicyApi);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
