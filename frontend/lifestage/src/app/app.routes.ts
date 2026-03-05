import { Routes } from '@angular/router';
import { authGuard } from './core/authGuard/auth-guard';
import { LoginButton } from './features/auth/login-button/login-button';
import { Lifeevents } from './features/lifeevents/ui/lifeevents';
import { NotFound } from './features/not-found/not-found';

export const routes: Routes = [
	{
		path: '',
		component: LoginButton,
		pathMatch: 'full',
	},
	{
		path: 'lifeevents',
		loadComponent: () =>
			import('./features/lifeevents/ui/lifeevents').then((l) => l.Lifeevents),
		canActivate: [authGuard],
	},
	{
		path: '**',
		component: NotFound,
	},
];
