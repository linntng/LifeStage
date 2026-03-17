import { TestBed } from '@angular/core/testing';

import { Adminstore } from './adminstore';

describe('Adminstore', () => {
	let service: Adminstore;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(Adminstore);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
