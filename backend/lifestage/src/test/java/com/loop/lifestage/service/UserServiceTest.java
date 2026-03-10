package com.loop.lifestage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.loop.lifestage.dto.UserDTO;
import com.loop.lifestage.exception.BadRequestException;
import com.loop.lifestage.exception.ResourceAlreadyExistsException;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.UserMapper;
import com.loop.lifestage.model.user.User;
import com.loop.lifestage.model.user.UserRole;
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
    void shouldReturnAllUsersWhenAdminRequests() {

      // Given
      User admin = createUser();
      admin.setRole(UserRole.ADMIN);

      when(userRepository.findById("1")).thenReturn(Optional.of(admin));
      when(userRepository.findAll()).thenReturn(java.util.List.of(user));
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      // When
      var result = userService.getAllUsers("1");

      // Then
      assertEquals(1, result.size());
      assertEquals(userDTO, result.get(0));

      verify(userRepository).findById("1");
      verify(userRepository).findAll();
    }

    @Test
    void shouldThrowRuntimeExceptionWhenAdminNotFound() {

      // Given
      when(userRepository.findById("1")).thenReturn(Optional.empty());

      // Then
      assertThrows(RuntimeException.class, () -> userService.getAllUsers("1"));

      verify(userRepository).findById("1");
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUserIsNotAdmin() {

      // Given
      User normalUser = createUser();
      normalUser.setRole(UserRole.USER);

      when(userRepository.findById("1")).thenReturn(Optional.of(normalUser));

      // Then
      assertThrows(RuntimeException.class, () -> userService.getAllUsers("1"));

      verify(userRepository).findById("1");
      verify(userRepository, never()).findAll();
    }

    @Test
    void shouldThrowRuntimeExceptionWhenRepositoryFails() {

      // Given
      when(userRepository.findById("1")).thenThrow(new RuntimeException());

      // Then
      assertThrows(RuntimeException.class, () -> userService.getAllUsers("1"));
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
      userDTO.setLifeEventIds(new java.util.HashSet<>());

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
      userDTO.setLifeEventIds(new java.util.HashSet<>(java.util.List.of(eventId)));

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

    // =========================
    // MANAGE POLICIES
    // =========================

    @Nested
    class ManagePolicies {

      @Test
      void addPolicyToUser_shouldAddPolicyAndPersistUser() {

        // Given
        Long policyId = 20L;
        userDTO.setPolicyIds(new java.util.HashSet<>());

        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        // When
        UserDTO result = userService.addPolicyToUser(userDTO, policyId);

        // Then
        assertTrue(userDTO.getPolicyIds().contains(policyId));
        assertEquals(userDTO, result);

        verify(userRepository).save(user);
      }

      @Test
      void removePolicyForUser_shouldRemovePolicyAndPersistUser() {

        // Given
        Long policyId = 30L;
        userDTO.setPolicyIds(new java.util.HashSet<>(java.util.List.of(policyId)));

        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        // When
        UserDTO result = userService.removePolicyForUser(userDTO, policyId);

        // Then
        assertFalse(userDTO.getPolicyIds().contains(policyId));
        assertEquals(userDTO, result);

        verify(userRepository).save(user);
      }

      @Test
      void addPolicyToUser_shouldThrowRuntimeException_whenUpdateFails() {

        // Given
        Long policyId = 40L;
        userDTO.setPolicyIds(new java.util.HashSet<>());

        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenThrow(new RuntimeException());

        // Then
        assertThrows(RuntimeException.class, () -> userService.addPolicyToUser(userDTO, policyId));
      }

      @Test
      void removePolicyForUser_shouldThrowRuntimeException_whenUpdateFails() {

        // Given
        Long policyId = 50L;
        userDTO.setPolicyIds(new java.util.HashSet<>(java.util.List.of(policyId)));

        when(userMapper.toUser(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenThrow(new RuntimeException());

        // Then
        assertThrows(
            RuntimeException.class, () -> userService.removePolicyForUser(userDTO, policyId));
      }
    }

    // =========================
    // CHANGE USER ROLE
    // =========================

    @Nested
    class ChangeRoleOfUser {

      @Test
      void shouldChangeUserRoleWhenAdminRequests() {

        // Given
        User admin = createUser();
        admin.setRole(UserRole.ADMIN);

        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(userRepository.findById("admin1")).thenReturn(Optional.of(admin));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserDTO(user)).thenReturn(userDTO);

        // When
        UserDTO result = userService.changeRoleOfUser("user1", "admin1", UserRole.ADMIN);

        // Then
        assertEquals(userDTO, result);
        assertEquals(UserRole.ADMIN, user.getRole());

        verify(userRepository).save(user);
      }

      @Test
      void shouldThrowRuntimeExceptionWhenUserNotFound() {

        // Given
        when(userRepository.findById("user1")).thenReturn(Optional.empty());

        // Then
        assertThrows(
            RuntimeException.class,
            () -> userService.changeRoleOfUser("user1", "admin1", UserRole.ADMIN));
      }

      @Test
      void shouldThrowRuntimeExceptionWhenAdminNotFound() {

        // Given
        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(userRepository.findById("admin1")).thenReturn(Optional.empty());

        // Then
        assertThrows(
            RuntimeException.class,
            () -> userService.changeRoleOfUser("user1", "admin1", UserRole.ADMIN));
      }

      @Test
      void shouldThrowRuntimeExceptionWhenRequesterIsNotAdmin() {

        // Given
        User normalUser = createUser();
        normalUser.setRole(UserRole.USER);

        when(userRepository.findById("user1")).thenReturn(Optional.of(user));
        when(userRepository.findById("admin1")).thenReturn(Optional.of(normalUser));

        // Then
        assertThrows(
            RuntimeException.class,
            () -> userService.changeRoleOfUser("user1", "admin1", UserRole.ADMIN));

        verify(userRepository, never()).save(user);
      }

      @Test
      void shouldThrowRuntimeExceptionWhenRepositoryFails() {

        // Given
        when(userRepository.findById("user1")).thenThrow(new RuntimeException());

        // Then
        assertThrows(
            RuntimeException.class,
            () -> userService.changeRoleOfUser("user1", "admin1", UserRole.ADMIN));
      }
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
