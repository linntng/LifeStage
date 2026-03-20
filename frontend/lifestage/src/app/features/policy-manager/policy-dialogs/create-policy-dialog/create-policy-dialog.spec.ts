import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePolicyDialog } from './create-policy-dialog';

describe('CreatePolicyDialog', () => {
	let component: CreatePolicyDialog;
	let fixture: ComponentFixture<CreatePolicyDialog>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [CreatePolicyDialog],
		}).compileComponents();

		fixture = TestBed.createComponent(CreatePolicyDialog);
		component = fixture.componentInstance;
		await fixture.whenStable();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
