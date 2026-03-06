import { TestBed } from '@angular/core/testing';
import { vi } from 'vitest';
import { LifeeventStore } from './lifeevent-store';
import { LifeeventApi } from './lifeevent-api';
import { of } from 'rxjs';

describe('LifeeventStore', () => {
	let service: LifeeventStore;
	let lifeeventApi: LifeeventApi;
	let lifeeventApiSpy: ReturnType<typeof vi.spyOn>;

	const mockLifeevents = [
		{ id: 1, name: 'Graduation' },
		{ id: 2, name: 'Marriage' },
		{ id: 3, name: 'Retirement' },
	];

	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [LifeeventApi],
		});
		service = TestBed.inject(LifeeventStore);
		lifeeventApi = TestBed.inject(LifeeventApi);
		lifeeventApiSpy = vi
			.spyOn(lifeeventApi, 'getLifeevents')
			.mockReturnValue(of(mockLifeevents));
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});

	it('should call the api to load lifeevents', () => {
		service.loadLifeevents();
		expect(lifeeventApiSpy).toHaveBeenCalled();
	});

	it('should load life events', () => {
		expect(service.lifeevents()).toEqual([]);
		service.loadLifeevents();

		expect(service.lifeevents()).toEqual(mockLifeevents);
	});
});
