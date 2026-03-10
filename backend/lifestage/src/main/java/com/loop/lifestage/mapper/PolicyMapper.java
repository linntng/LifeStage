package com.loop.lifestage.mapper;

import com.loop.lifestage.dto.PolicyDTO;
import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.model.policy.Policy;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface PolicyMapper {

  @Mapping(
      source = "coveredLifeEvents",
      target = "coveredLifeEvents",
      qualifiedByName = "lifeEventsToIds")
  PolicyDTO toPolicyDTO(Policy policy);

  @Mapping(
      source = "coveredLifeEvents",
      target = "coveredLifeEvents",
      qualifiedByName = "idsToLifeEvents")
  Policy toPolicy(PolicyDTO policyDTO);

  @Named("lifeEventsToIds")
  default Set<Long> lifeEventsToIds(Set<LifeEvent> lifeEvents) {
    if (lifeEvents == null) {
      return null;
    }
    return lifeEvents.stream().map(LifeEvent::getId).collect(Collectors.toSet());
  }

  @Named("idsToLifeEvents")
  default Set<LifeEvent> idsToLifeEvents(Set<Long> ids) {
    if (ids == null) {
      return null;
    }
    return ids.stream()
        .map(
            id -> {
              LifeEvent lifeEvent = new LifeEvent();
              lifeEvent.setId(id);
              return lifeEvent;
            })
        .collect(Collectors.toSet());
  }
}
