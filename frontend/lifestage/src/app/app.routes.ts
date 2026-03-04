import { Routes } from '@angular/router';

export const routes: Routes = [
	{
		path: 'lifeevents',
		loadComponent: () =>
			import('./features/lifeevents/ui/lifeevents').then((l) => l.Lifeevents),
	},
];
