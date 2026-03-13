package com.loop.lifestage.engine;

import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.model.policy.EditAction;
import com.loop.lifestage.model.policy.Policy;
import com.loop.lifestage.model.policy.PolicyEditAction;
import com.loop.lifestage.model.policy.PolicyRecommendation;
import com.loop.lifestage.model.user.User;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.loop.lifestage.repository.PolicyRepository;

@Component
public class PolicyRecommendationEngine {
    private final PolicyRepository policyRepository;

    public PolicyRecommendationEngine(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public PolicyRecommendation generateRecommendation(User user, LifeEvent lifeEvent, PolicyRecommendation recommendation) {
        Set<PolicyEditAction> editActions = generateActions(user);
        float premiumImpact = calculatePremiumImpact(editActions);
        recommendation.setPremiumImpact(premiumImpact);
        recommendation.setUser(user);
        recommendation.setLifeEvent(lifeEvent);
        for (PolicyEditAction policyEditAction : editActions) {
            recommendation.addEditAction(policyEditAction);
        }
        return recommendation;
    }

    private Set<PolicyEditAction> generateActions(User user) {
        List<Policy> allPolicies = policyRepository.findAll();
        Set<Long> lifeEventIds = user.getLifeEvents().stream().map(l -> l.getId()).collect(Collectors.toSet());
        Set<Long> bestPolicyPlan = getBestPolicyPlan(lifeEventIds, allPolicies);
        Set<Long> currentPolicies = user.getPolicies().stream().map(p -> p.getId()).collect(Collectors.toSet());
        Set<Long> policiesToRemove = setDifference(currentPolicies, bestPolicyPlan);
        Set<Long> policiesToAdd = setDifference(bestPolicyPlan, currentPolicies);
        Set<PolicyEditAction> editActions = new HashSet<>();
        for (Long id : policiesToRemove) {
            Policy policy = allPolicies.stream()
                                       .filter(p -> p.getId().equals(id))
                                       .findFirst().orElse(null);
            editActions.add(new PolicyEditAction(policy, EditAction.REMOVE));
        }
        for (Long id : policiesToAdd) {
            Policy policy = allPolicies.stream()
                                       .filter(p -> p.getId().equals(id))
                                       .findFirst().orElse(null);
            editActions.add(new PolicyEditAction(policy, EditAction.ADD));
        }
        return editActions;
    }

    private float calculatePremiumImpact(Set<PolicyEditAction> editActions) {
        float premiumImpact = 0;
        for (PolicyEditAction policyEditAction : editActions) {
            if (policyEditAction.getAction() == EditAction.ADD) {
                premiumImpact += policyEditAction.getPolicy().getPremium();
            } else {
                premiumImpact -= policyEditAction.getPolicy().getPremium();
            }
        }
        return premiumImpact;
    }
    
    private Set<Long> setDifference(Set<Long> A, Set<Long> B) {
        Set<Long> difference = new HashSet<>();
        for (Long element : A) {
            if (!B.contains(element)) {
                difference.add(element);
            }
        }
        return difference;
    }

    private Set<Long> getBestPolicyPlan(Set<Long> lifeEventIds, List<Policy> policies) {
        Set<Long> policyIds = new HashSet<>();
        policies.sort(
            Comparator.comparingInt((Policy p) -> p.getCoveredLifeEvents().size())
                      .reversed()
        );
        for (Policy policy : policies) {
            Set<Long> coveredLifeEventIds = policy.getCoveredLifeEvents()
                                               .stream()
                                               .map(l -> l.getId())
                                               .collect(Collectors.toSet());
            if (lifeEventIds.containsAll(coveredLifeEventIds)) {
                lifeEventIds.removeAll(coveredLifeEventIds);
                policyIds.add(policy.getId());
            }
        }
        return policyIds;
    }
}