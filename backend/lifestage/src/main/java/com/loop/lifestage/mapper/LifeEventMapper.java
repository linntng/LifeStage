package com.loop.lifestage.mapper;

import com.loop.lifestage.dto.LifeEventDTO;
import com.loop.lifestage.model.LifeEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LifeEventMapper {
  LifeEventDTO toLifeEventDTO(LifeEvent lifeEvent);

  LifeEvent toLifeEvent(LifeEventDTO lifeEventDTO);
}
