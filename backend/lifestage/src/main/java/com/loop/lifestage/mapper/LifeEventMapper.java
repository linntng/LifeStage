
package com.loop.lifestage.mapper;
import org.mapstruct.Mapper;

import com.loop.lifestage.dto.LifeEventDTO;
import com.loop.lifestage.model.LifeEvent;

@Mapper(componentModel = "spring")
public interface LifeEventMapper {
    LifeEventDTO toLifeEventDTO(LifeEvent lifeEvent);
    
    LifeEvent toLifeEvent(LifeEventDTO lifeEventDTO);
}