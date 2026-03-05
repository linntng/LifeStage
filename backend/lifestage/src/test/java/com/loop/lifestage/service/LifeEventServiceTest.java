package com.loop.lifestage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.loop.lifestage.dto.LifeEventDTO;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.LifeEventMapper;
import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.repository.LifeEventRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LifeEventServiceTest {

@Mock private LifeEventRepository lifeEventRepository;

@Mock private LifeEventMapper lifeEventMapper;

@InjectMocks private LifeEventService lifeEventService;

private LifeEvent lifeEvent;
private LifeEventDTO lifeEventDTO;

@BeforeEach
void setUp() {
	lifeEvent = createLifeEvent();
	lifeEventDTO = createLifeEventDTO();
}

// =========================
// GET ALL LIFE EVENTS
// =========================

@Test
void getAllLifeEvents_shouldReturnMappedLifeEvents() {

	// Given
	when(lifeEventRepository.findAll()).thenReturn(List.of(lifeEvent));
	when(lifeEventMapper.toLifeEventDTO(lifeEvent)).thenReturn(lifeEventDTO);

	// When
	List<LifeEventDTO> result = lifeEventService.getAllLifeEvents();

	// Then
	assertEquals(1, result.size());
	assertEquals(lifeEventDTO.getId(), result.get(0).getId());
	assertEquals(lifeEventDTO.getName(), result.get(0).getName());

	verify(lifeEventRepository).findAll();
	verify(lifeEventMapper).toLifeEventDTO(lifeEvent);
}

@Test
void getAllLifeEvents_shouldThrowResourceNotFound_whenEntityNotFoundExceptionOccurs() {

	// Given
	when(lifeEventRepository.findAll()).thenThrow(new EntityNotFoundException());

	// Then
	assertThrows(ResourceNotFoundException.class, () -> lifeEventService.getAllLifeEvents());
}

@Test
void getAllLifeEvents_shouldThrowRuntimeException_whenUnexpectedExceptionOccurs() {

	// Given
	when(lifeEventRepository.findAll()).thenThrow(new RuntimeException("database error"));

	// Then
	assertThrows(RuntimeException.class, () -> lifeEventService.getAllLifeEvents());
}

// =========================
// TEST DATA FACTORIES
// =========================

private LifeEvent createLifeEvent() {
	LifeEvent event = new LifeEvent();
	event.setId(1L);
	event.setName("Graduation");
	event.setUsers(java.util.Collections.emptyList());
	return event;
}

private LifeEventDTO createLifeEventDTO() {
	return new LifeEventDTO(1L, "Graduation");
}
}
