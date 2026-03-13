import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaseDialog } from './case-dialog';

describe('CaseDialog', () => {
	let component: CaseDialog;
	let fixture: ComponentFixture<CaseDialog>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [CaseDialog],
		}).compileComponents();

		fixture = TestBed.createComponent(CaseDialog);
		component = fixture.componentInstance;
		await fixture.whenStable();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
