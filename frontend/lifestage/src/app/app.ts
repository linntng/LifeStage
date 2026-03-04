import { Component, inject, signal, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LifeeventStore } from './features/lifeevents/state/lifeevent-store';
import { Lifeevents } from './features/lifeevents/ui/lifeevents';

@Component({
	selector: 'app-root',
	imports: [RouterOutlet, Lifeevents],
	templateUrl: './app.html',
	styleUrl: './app.css',
})
export class App implements OnInit {
	protected readonly title = signal('lifestage');
	lifeeventStore = inject(LifeeventStore);
	
	ngOnInit() {
		this.lifeeventStore.loadLifeevents();
	}
}
