import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PolicyManager } from './policy-manager';

describe('PolicyManager', () => {
	let component: PolicyManager;
	let fixture: ComponentFixture<PolicyManager>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [PolicyManager],
		}).compileComponents();

		fixture = TestBed.createComponent(PolicyManager);
		component = fixture.componentInstance;
		await fixture.whenStable();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
