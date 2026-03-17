export interface PolicyEditAction {
	id: number;
	policyId: number;
	policyRecommendationId: number;
	action: 'ADD' | 'REMOVE';
}
