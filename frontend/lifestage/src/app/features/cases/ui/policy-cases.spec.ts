import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PolicyCases } from './policy-cases';

describe('PolicyCases', () => {
	let component: PolicyCases;
	let fixture: ComponentFixture<PolicyCases>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [PolicyCases],
		}).compileComponents();

		fixture = TestBed.createComponent(PolicyCases);
		component = fixture.componentInstance;
		await fixture.whenStable();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
