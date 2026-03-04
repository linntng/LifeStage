import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { LifeeventStore } from '../state/lifeevent-store';
import { CommonModule, NgFor } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
	selector: 'app-lifeevents',
	imports: [CommonModule, RouterLink],
	templateUrl: './lifeevents.html',
})
export class Lifeevents {
	lifeeventStore = inject(LifeeventStore);

	ngOnInit() {
		this.lifeeventStore.getLifeevents();
	}
}
