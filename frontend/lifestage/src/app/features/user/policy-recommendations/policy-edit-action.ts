import { Injectable } from '@angular/core';

export interface PolicyEditActions {
	id: number;
	policyId: number;
	policyRecommendationId: number;
	action: 'ADD' | 'REMOVE';
}

@Injectable({
	providedIn: 'root',
})
export class PolicyEditAction {}
