package com.loop.lifestage.mapper;

import com.loop.lifestage.dto.UserDTO;
import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.model.User;
import com.loop.lifestage.model.policy.Policy;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(source = "lifeEvents", target = "lifeEventIds", qualifiedByName = "lifeEventsToIds")
  @Mapping(source = "policies", target = "policyIds", qualifiedByName = "policiesToIds")
  UserDTO toUserDTO(User user);

  @Mapping(source = "lifeEventIds", target = "lifeEvents", qualifiedByName = "idsToLifeEvents")
  @Mapping(source = "policyIds", target = "policies", qualifiedByName = "idsToPolicies")
  User toUser(UserDTO userDTO);

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

  @Named("policiesToIds")
  default Set<Long> policiesToIds(Set<Policy> policies) {
    if (policies == null) {
      return null;
    }
    return policies.stream().map(Policy::getId).collect(Collectors.toSet());
  }

  @Named("idsToPolicies")
  default Set<Policy> idsToPolicies(Set<Long> ids) {
    if (ids == null) {
      return null;
    }
    return ids.stream()
        .map(
            id -> {
              Policy lifeEvent = new Policy();
              lifeEvent.setId(id);
              return lifeEvent;
            })
        .collect(Collectors.toSet());
  }
}
