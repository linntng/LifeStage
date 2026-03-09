import { Routes } from '@angular/router';
import { authGuard } from './core/authGuard/auth-guard';
import { NotFound } from './features/not-found/not-found';
import { Dashboard } from './features/dashboard/dashboard';

export const routes: Routes = [
	{
		path: '',
		component: Dashboard,
		pathMatch: 'full',
	},
	{
		path: 'lifeevents',
		loadComponent: () =>
			import('./features/lifeevents/ui/lifeevents').then((l) => l.Lifeevents),
		canActivate: [authGuard],
	},
	{
		path: 'policies',
		loadComponent: () => import('./features/policies/ui/policies').then((p) => p.Policies),
		canActivate: [authGuard],
	},
	{
		path: '**',
		component: NotFound,
	},
];
