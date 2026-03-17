import { TestBed } from '@angular/core/testing';

import { UserLifeeventApi } from './user-lifeevent-api';

describe('UserLifeeventApi', () => {
	let service: UserLifeeventApi;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(UserLifeeventApi);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
