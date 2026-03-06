package com.loop.lifestage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.loop.lifestage.dto.UserDTO;
import com.loop.lifestage.exception.BadRequestException;
import com.loop.lifestage.exception.ResourceAlreadyExistsException;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.UserMapper;
import com.loop.lifestage.model.User;
import com.loop.lifestage.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserService userService;

  private User user;
  private UserDTO userDTO;

  @BeforeEach
  void setup() {
    user = createUser();
    userDTO = createUserDTO();
  }

  // =========================
  // CREATE USER
  // =========================

  @Nested
  class CreateUser {

    @Test
    void shouldCreateUserSuccessfully() {

      // Given
      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenReturn(user);
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      // When
      UserDTO result = userService.createUser(userDTO);

      // Then
      assertEquals(userDTO, result);
      verify(userRepository).save(user);
    }

    @Test
    void shouldThrowResourceAlreadyExistsWhenConstraintViolation() {

      // Given
      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenThrow(new DataIntegrityViolationException("duplicate"));

      // Then
      assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void shouldThrowBadRequestWhenMapperThrowsIllegalArgument() {

      // Given
      when(userMapper.toUser(userDTO)).thenThrow(new IllegalArgumentException("invalid"));

      // Then
      assertThrows(BadRequestException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnexpectedErrorOccurs() {

      // Given
      when(userMapper.toUser(userDTO)).thenThrow(new RuntimeException("unexpected"));

      // Then
      assertThrows(RuntimeException.class, () -> userService.createUser(userDTO));
    }
  }

  // =========================
  // UPDATE USER
  // =========================

  @Nested
  class UpdateUser {

    @Test
    void shouldUpdateUserSuccessfully() {

      // Given
      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenReturn(user);
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      // When
      UserDTO result = userService.updateUser(userDTO);

      // Then
      assertEquals(userDTO, result);
      verify(userRepository).save(user);
    }

    @Test
    void shouldThrowBadRequestWhenMapperThrowsIllegalArgument() {

      // Given
      when(userMapper.toUser(userDTO)).thenThrow(new IllegalArgumentException());

      // Then
      assertThrows(BadRequestException.class, () -> userService.updateUser(userDTO));
    }

    @Test
    void shouldThrowResourceNotFoundWhenRepositoryThrowsEntityNotFound() {

      // Given
      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenThrow(new EntityNotFoundException());

      // Then
      assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userDTO));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnexpectedErrorOccurs() {

      // Given
      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenThrow(new RuntimeException());

      // Then
      assertThrows(RuntimeException.class, () -> userService.updateUser(userDTO));
    }
  }

  // =========================
  // GET USER BY ID
  // =========================

  @Nested
  class GetUserById {

    @Test
    void shouldReturnUserWhenFound() {

      // Given
      when(userRepository.findById("1")).thenReturn(Optional.of(user));
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      // When
      UserDTO result = userService.getUserById("1");

      // Then
      assertEquals(userDTO, result);
      verify(userRepository).findById("1");
    }

    @Test
    void shouldThrowResourceNotFoundWhenUserDoesNotExist() {

      // Given
      when(userRepository.findById("1")).thenReturn(Optional.empty());

      // Then
      assertThrows(ResourceNotFoundException.class, () -> userService.getUserById("1"));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnexpectedErrorOccurs() {

      // Given
      when(userRepository.findById("1")).thenThrow(new RuntimeException());

      // Then
      assertThrows(RuntimeException.class, () -> userService.getUserById("1"));
    }
  }

  // =========================
  // GET ALL USERS
  // =========================

  @Nested
  class GetAllUsers {

    @Test
    void shouldReturnUserList() {

      // Given
      when(userRepository.findAll()).thenReturn(java.util.List.of(user));
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      // When
      var result = userService.getAllUsers();

      // Then
      assertEquals(1, result.size());
      assertEquals(userDTO, result.get(0));
      verify(userRepository).findAll();
    }

    @Test
    void shouldThrowRuntimeExceptionWhenRepositoryFails() {

      // Given
      when(userRepository.findAll()).thenThrow(new RuntimeException());

      // Then
      assertThrows(RuntimeException.class, () -> userService.getAllUsers());
    }
  }

  // =========================
  // DELETE USER
  // =========================

  @Nested
  class DeleteUser {

    @Test
    void shouldDeleteUserSuccessfully() {

      // Given
      when(userMapper.toUser(userDTO)).thenReturn(user);

      // When
      userService.deleteUser(userDTO);

      // Then
      verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDeleteFails() {

      // Given
      when(userMapper.toUser(userDTO)).thenReturn(user);
      doThrow(new RuntimeException()).when(userRepository).delete(user);

      // Then
      assertThrows(RuntimeException.class, () -> userService.deleteUser(userDTO));
    }
  }

  @Nested
  class ManageLifeEvents {

    @Test
    void addLifeEventToUser_shouldAddEventAndPersistUser() {

      // Given
      Long eventId = 10L;
      userDTO.setLifeEventIds(new java.util.ArrayList<>());

      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenReturn(user);
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      // When
      UserDTO result = userService.addLifeEventToUser(userDTO, eventId);

      // Then
      assertTrue(userDTO.getLifeEventIds().contains(eventId));
      assertEquals(userDTO, result);

      verify(userRepository).save(user);
    }

    @Test
    void removeLifeEventForUser_shouldRemoveEventAndPersistUser() {

      // Given
      Long eventId = 5L;
      userDTO.setLifeEventIds(new java.util.ArrayList<>(java.util.List.of(eventId)));

      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenReturn(user);
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      // When
      UserDTO result = userService.removeLifeEventForUser(userDTO, eventId);

      // Then
      assertFalse(userDTO.getLifeEventIds().contains(eventId));
      assertEquals(userDTO, result);

      verify(userRepository).save(user);
    }
  }

  // =========================
  // TEST DATA FACTORIES
  // =========================

  private User createUser() {
    User u = new User();
    u.setId("1");
    return u;
  }

  private UserDTO createUserDTO() {
    return new UserDTO("1", "testuser");
  }
}
