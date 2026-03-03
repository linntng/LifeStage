import { TestBed } from '@angular/core/testing';

import { LifeeventApi } from './lifeevent-api';

describe('LifeeventApi', () => {
	let service: LifeeventApi;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(LifeeventApi);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
