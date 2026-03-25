import { Component, Input, HostBinding } from '@angular/core';
import { MatCardModule } from '@angular/material/card';

@Component({
	selector: 'app-info-card',
	imports: [MatCardModule],
	templateUrl: './info-card.html',
})
export class InfoCard {
	@Input() title = '';
	@Input() color = 'bg-secondary';

	@HostBinding('class')
	hostClasses = 'block h-full';
}
