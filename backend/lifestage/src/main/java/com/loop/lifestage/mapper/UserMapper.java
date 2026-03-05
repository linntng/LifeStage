package com.loop.lifestage.mapper;

import com.loop.lifestage.dto.UserDTO;
import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.model.User;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserMapper {
@Mapping(source = "lifeEvents", target = "lifeEventIds", qualifiedByName = "lifeEventsToIds")
UserDTO toUserDTO(User user);

@Mapping(source = "lifeEventIds", target = "lifeEvents", qualifiedByName = "idsToLifeEvents")
User toUser(UserDTO userDTO);

@Named("lifeEventsToIds")
default List<Long> lifeEventsToIds(List<LifeEvent> lifeEvents) {
	if (lifeEvents == null) {
	return null;
	}
	return lifeEvents.stream().map(LifeEvent::getId).collect(Collectors.toList());
}

@Named("idsToLifeEvents")
default List<LifeEvent> idsToLifeEvents(List<Long> ids) {
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
		.collect(Collectors.toList());
}
}
