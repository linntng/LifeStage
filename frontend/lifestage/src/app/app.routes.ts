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
		path: 'admin',
		loadComponent: () => import('./features/admin/admin').then((a) => a.Admin),
		canActivate: [authGuard],
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
		path: 'cases',
		loadComponent: () => import('./features/cases/ui/policy-cases').then((c) => c.PolicyCases),
		canActivate: [authGuard],
	},
	{
		path: 'policy-manager',
		loadComponent: () =>
			import('./features/policy-manager/policy-manager').then((p) => p.PolicyManager),
		canActivate: [authGuard],
	},
	{
		path: '**',
		component: NotFound,
	},
];
