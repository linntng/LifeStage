import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewPolicyDialog } from './review-policy-dialog';

describe('ReviewPolicyDialog', () => {
	let component: ReviewPolicyDialog;
	let fixture: ComponentFixture<ReviewPolicyDialog>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [ReviewPolicyDialog],
		}).compileComponents();

		fixture = TestBed.createComponent(ReviewPolicyDialog);
		component = fixture.componentInstance;
		await fixture.whenStable();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
