import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { LifeeventStore } from '../state/lifeevent-store';
import { CommonModule, NgFor } from '@angular/common';

@Component({
	selector: 'app-lifeevents',
	imports: [],
	templateUrl: './lifeevents.html',
})
export class Lifeevents {
	private lifeeventStore = inject(LifeeventStore);

	getLifeEvents() {
		return this.lifeeventStore.getLifeevents();
	}
}
