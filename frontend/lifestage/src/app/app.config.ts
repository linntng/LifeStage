import { ApplicationConfig, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideKeycloak } from 'keycloak-angular';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
	providers: [
		provideKeycloak({
			config: {
				url: 'https://lemur-9.cloud-iam.com/auth',
				realm: 'lifestage',
				clientId: 'lifestage',
			},
			initOptions: {
				onLoad: 'check-sso',
				silentCheckSsoRedirectUri: window.location.origin + '/silent-check-sso.html',
			},
		}),
		provideBrowserGlobalErrorListeners(),
		provideRouter(routes),
	],
};
