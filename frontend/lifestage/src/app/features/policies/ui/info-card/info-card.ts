import { Component, inject, Input } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { Policy } from '../../state/policies-api';
import { PoliciesStore } from '../../state/policies-store';
import { Auth } from '../../../../core/auth/auth';
import { UserStore } from '../../../user/state/user-store';

@Component({
	selector: 'app-info-card',
	imports: [MatCardModule],
	templateUrl: './info-card.html',
})
export class InfoCard {
	@Input() policy!: Policy;

	policyStore = inject(PoliciesStore);
	userStore = inject(UserStore);
	auth = inject(Auth);
}
