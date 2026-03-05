import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core/primitives/di';
import { Auth } from '../auth/auth';

export const authGuard: CanActivateFn = async (route, state) => {
	const auth = inject(Auth);
	const router = inject(Router);


	if (!auth.authenticated()) {
		auth.login()
		return router.navigate(['/']);
	}

	return auth.authenticated();
};
