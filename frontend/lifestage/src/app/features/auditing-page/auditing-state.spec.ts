import { TestBed } from '@angular/core/testing';

import { AuditingState } from './auditing-state';

describe('AuditingState', () => {
	let service: AuditingState;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(AuditingState);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
