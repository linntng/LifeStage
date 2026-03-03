import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Lifeevents } from './lifeevents.ts';

describe('Lifeevents', () => {
	let component: Lifeevents;
	let fixture: ComponentFixture<Lifeevents>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [Lifeevents],
		}).compileComponents();

		fixture = TestBed.createComponent(Lifeevents);
		component = fixture.componentInstance;
		await fixture.whenStable();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
