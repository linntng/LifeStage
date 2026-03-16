import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PolicyDialog } from './dialog';

describe('PolicyDialog', () => {
	let component: PolicyDialog;
	let fixture: ComponentFixture<PolicyDialog>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [PolicyDialog],
		}).compileComponents();

		fixture = TestBed.createComponent(PolicyDialog);
		component = fixture.componentInstance;
		await fixture.whenStable();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
