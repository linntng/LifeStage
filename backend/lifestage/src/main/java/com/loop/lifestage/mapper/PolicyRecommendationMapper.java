package com.loop.lifestage.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.AfterMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import com.loop.lifestage.dto.PolicyRecommendationDTO;
import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.model.policy.PolicyEditAction;
import com.loop.lifestage.model.policy.PolicyRecommendation;
import com.loop.lifestage.model.user.User;
import com.loop.lifestage.repository.LifeEventRepository;
import com.loop.lifestage.repository.UserRepository;

@Mapper(
    componentModel = "spring",
    uses = PolicyEditActionMapper.class
)
public abstract class PolicyRecommendationMapper {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected LifeEventRepository lifeEventRepository;

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToId")
    @Mapping(source = "lifeEvent", target = "lifeEventId", qualifiedByName = "lifeEventToId")
    @Mapping(source = "editActions", target = "policyEditActions")
    public abstract PolicyRecommendationDTO toDto(PolicyRecommendation recommendation);

    @Mapping(source = "userId", target = "user", qualifiedByName = "idToUser")
    @Mapping(source = "lifeEventId", target = "lifeEvent", qualifiedByName = "idToLifeEvent")
    @Mapping(source = "policyEditActions", target = "editActions")
    public abstract PolicyRecommendation toEntity(PolicyRecommendationDTO dto);

    @Named("userToId")
    protected String userToId(User user) {
        return user != null ? user.getId() : null;
    }

    @Named("lifeEventToId")
    protected Long lifeEventToId(LifeEvent lifeEvent) {
        return lifeEvent != null ? lifeEvent.getId() : null;
    }

    @Named("idToUser")
    protected User idToUser(String id) {
        if (id == null) {
            return null;
        }
        return userRepository.getReferenceById(id);
    }

    @Named("idToLifeEvent")
    protected LifeEvent idToLifeEvent(Long id) {
        if (id == null) {
            return null;
        }
        return lifeEventRepository.getReferenceById(id);
    }

    @AfterMapping
    protected void linkChildren(@MappingTarget PolicyRecommendation entity) {
        if (entity.getEditActions() != null) {
            for (PolicyEditAction action : entity.getEditActions()) {
                action.setPolicyRecommendation(entity);
            }
        }
    }
}