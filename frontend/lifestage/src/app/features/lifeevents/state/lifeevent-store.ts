import { inject, Injectable, signal } from '@angular/core';
import { LifeeventApi } from './lifeevent-api';

@Injectable({
	providedIn: 'root',
})
export class LifeeventStore {
	private lifeeventsApi = inject(LifeeventApi);
	readonly lifeevents = signal<string[]>([]);
	readonly loading = signal(false);

	getLifeevents() {
		this.lifeeventsApi.getLifeevents().subscribe({
			next: (lifeevents) => {
				this.lifeevents.set(lifeevents.map((lifeevent) => lifeevent.name));
			},
			error: () => {
				console.error('Failed to fetch lifeevents');
			},
			complete: () => {
				this.loading.set(false);
			},
		});
	}

	getLifeeventById(id: number) {
		return this.lifeeventsApi.getLifeeventById(id);
	}
}
