import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuditingPage } from './auditing-page';

describe('AuditingPage', () => {
	let component: AuditingPage;
	let fixture: ComponentFixture<AuditingPage>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [AuditingPage],
		}).compileComponents();

		fixture = TestBed.createComponent(AuditingPage);
		component = fixture.componentInstance;
		await fixture.whenStable();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
