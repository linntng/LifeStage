import { CanActivateFn } from '@angular/router';
import { Auth } from '../auth/auth';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = async () => {
	const auth = inject(Auth);

	if (!auth.authenticated()) {
		auth.login();
		return false;
	}

	return auth.authenticated();
};
