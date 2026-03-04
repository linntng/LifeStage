import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';

import { LifeeventApi } from '././lifeevent-api';

describe('LifeeventApi', () => {
	let service: LifeeventApi;
	let httpTestingController: HttpTestingController;

	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [provideHttpClientTesting()],
		});

		service = TestBed.inject(LifeeventApi);
		httpTestingController = TestBed.inject(HttpTestingController);
	});

	afterEach(() => {
		httpTestingController.verify(); // Verifies that no requests are outstanding
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});

	//
	// This test is not necessary, but serves as a good example of how to test API calls with HttpTestingController
	//
	it('should call the api to get life events', () => {
		const mockLifeevents = [
			{ id: 1, name: 'Graduation' },
			{ id: 2, name: 'Marriage' },
			{ id: 3, name: 'Retirement' },
		];

		service.getLifeevents().subscribe((lifeevents) => {
			expect(lifeevents).toEqual(mockLifeevents);
		});

		// Expect a single GET request to be sent to the correct URL
		const req = httpTestingController.expectOne(`${environment.apiUrl}/lifeevents`);

		expect(req.request.method).toEqual('GET'); // Verify that service sends a GET request

		req.flush(mockLifeevents); // Simulate the response with mock data
	});
});
