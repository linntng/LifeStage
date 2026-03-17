import { TestBed } from '@angular/core/testing';

import { PolicyRecommendationApi } from './policy-recommendation-api';

describe('PolicyRecommendationApi', () => {
	let service: PolicyRecommendationApi;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(PolicyRecommendationApi);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
