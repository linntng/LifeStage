import { inject, Injectable, signal } from '@angular/core';
import { Lifeevent, LifeeventApi } from './lifeevent-api';

@Injectable({
	providedIn: 'root',
})
export class LifeeventStore {
	private lifeeventsApi = inject(LifeeventApi);
	readonly lifeevents = signal<Lifeevent[]>([]);
	readonly loading = signal(false);

	loadLifeevents() {
		this.loading.set(true);
		this.lifeeventsApi.getLifeevents().subscribe({
			next: (lifeevents) => {
				this.lifeevents.set(lifeevents);
			},
			error: () => {
				console.error('Failed to fetch lifeevents');
			},
			complete: () => {
				this.loading.set(false);
			},
		});
	}

	getLifeevents() {
		return this.lifeevents();
	}

	getLifeeventById(id: number) {
		return this.lifeevents().find((lifeevent: Lifeevent) => lifeevent.id === id);
	}
}
