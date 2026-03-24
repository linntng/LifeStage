import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotLoggedIn } from './not-logged-in';

describe('NotLoggedIn', () => {
	let component: NotLoggedIn;
	let fixture: ComponentFixture<NotLoggedIn>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [NotLoggedIn],
		}).compileComponents();

		fixture = TestBed.createComponent(NotLoggedIn);
		component = fixture.componentInstance;
		await fixture.whenStable();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
