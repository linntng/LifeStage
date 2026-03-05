package com.loop.lifestage.service;

import com.loop.lifestage.dto.LifeEventDTO;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.LifeEventMapper;
import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.repository.LifeEventRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LifeEventService {

private final LifeEventRepository lifeEventRepository;
private final LifeEventMapper lifeEventMapper;

public LifeEventService(
	LifeEventRepository lifeEventRepository, LifeEventMapper lifeEventMapper) {
	this.lifeEventRepository = lifeEventRepository;
	this.lifeEventMapper = lifeEventMapper;
}

@Transactional(readOnly = true)
public List<LifeEventDTO> getAllLifeEvents() {
	try {
	List<LifeEvent> lifeEvents = lifeEventRepository.findAll();
	return lifeEvents.stream().map(lifeEventMapper::toLifeEventDTO).collect(Collectors.toList());
	} catch (EntityNotFoundException e) {
	throw new ResourceNotFoundException("Life events not found");
	} catch (Exception e) {
	throw new RuntimeException("An error occurred while fetching life events", e);
	}
}
}
