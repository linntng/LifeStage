import { TestBed } from '@angular/core/testing';

import { LifeeventStore } from './lifeevent-store';

describe('LifeeventStore', () => {
	let service: LifeeventStore;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(LifeeventStore);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
