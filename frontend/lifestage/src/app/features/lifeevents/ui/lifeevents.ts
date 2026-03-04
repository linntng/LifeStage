import { Component, inject } from '@angular/core';
import { LifeeventStore } from '../state/lifeevent-store';
import { CommonModule } from '@angular/common';

@Component({
	selector: 'app-lifeevents',
	imports: [CommonModule],
	templateUrl: './lifeevents.html',
})
export class Lifeevents {
	private lifeeventStore = inject(LifeeventStore);

	lifeevents = this.lifeeventStore.lifeevents;
}
