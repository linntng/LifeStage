package com.loop.lifestage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.loop.lifestage.dto.PolicyRecommendationDTO;
import com.loop.lifestage.dto.UserDTO;
import com.loop.lifestage.engine.PolicyRecommendationEngine;
import com.loop.lifestage.exception.BadRequestException;
import com.loop.lifestage.exception.ResourceAlreadyExistsException;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.PolicyRecommendationMapper;
import com.loop.lifestage.mapper.UserMapper;
import com.loop.lifestage.model.LifeEvent;
import com.loop.lifestage.model.policy.PolicyRecommendation;
import com.loop.lifestage.model.user.User;
import com.loop.lifestage.model.user.UserRole;
import com.loop.lifestage.repository.LifeEventRepository;
import com.loop.lifestage.repository.PolicyRecommendationRepository;
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
  @Mock private PolicyRecommendationRepository policyRecommendationRepository;
  @Mock private PolicyRecommendationMapper policyRecommendationMapper;
  @Mock private LifeEventRepository lifeEventRepository;
  @Mock private PolicyRecommendationEngine recommendationEngine;

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

      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenReturn(user);
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      UserDTO result = userService.createUser(userDTO);

      assertEquals(userDTO, result);
      verify(userRepository).save(user);
    }

    @Test
    void shouldThrowResourceAlreadyExistsWhenConstraintViolation() {

      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenThrow(new DataIntegrityViolationException("duplicate"));

      assertThrows(ResourceAlreadyExistsException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void shouldThrowBadRequestWhenMapperThrowsIllegalArgument() {

      when(userMapper.toUser(userDTO)).thenThrow(new IllegalArgumentException("invalid"));

      assertThrows(BadRequestException.class, () -> userService.createUser(userDTO));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnexpectedErrorOccurs() {

      when(userMapper.toUser(userDTO)).thenThrow(new RuntimeException("unexpected"));

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

      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenReturn(user);
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      UserDTO result = userService.updateUser(userDTO);

      assertEquals(userDTO, result);
      verify(userRepository).save(user);
    }

    @Test
    void shouldThrowBadRequestWhenMapperThrowsIllegalArgument() {

      when(userMapper.toUser(userDTO)).thenThrow(new IllegalArgumentException());

      assertThrows(BadRequestException.class, () -> userService.updateUser(userDTO));
    }

    @Test
    void shouldThrowResourceNotFoundWhenRepositoryThrowsEntityNotFound() {

      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenThrow(new EntityNotFoundException());

      assertThrows(ResourceNotFoundException.class, () -> userService.updateUser(userDTO));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnexpectedErrorOccurs() {

      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenThrow(new RuntimeException());

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

      when(userRepository.findById("1")).thenReturn(Optional.of(user));
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      UserDTO result = userService.getUserById("1");

      assertEquals(userDTO, result);
      verify(userRepository).findById("1");
    }

    @Test
    void shouldThrowResourceNotFoundWhenUserDoesNotExist() {

      when(userRepository.findById("1")).thenReturn(Optional.empty());

      assertThrows(ResourceNotFoundException.class, () -> userService.getUserById("1"));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUnexpectedErrorOccurs() {

      when(userRepository.findById("1")).thenThrow(new RuntimeException());

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

      User admin = createUser();
      admin.setRole(UserRole.ADMIN);

      when(userRepository.findById("1")).thenReturn(Optional.of(admin));
      when(userRepository.findAll()).thenReturn(java.util.List.of(user));
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      var result = userService.getAllUsers("1");

      assertEquals(1, result.size());
      assertEquals(userDTO, result.get(0));

      verify(userRepository).findById("1");
      verify(userRepository).findAll();
    }

    @Test
    void shouldThrowRuntimeExceptionWhenAdminNotFound() {

      when(userRepository.findById("1")).thenReturn(Optional.empty());

      assertThrows(RuntimeException.class, () -> userService.getAllUsers("1"));
    }

    @Test
    void shouldThrowRuntimeExceptionWhenUserIsNotAdmin() {

      User normalUser = createUser();
      normalUser.setRole(UserRole.USER);

      when(userRepository.findById("1")).thenReturn(Optional.of(normalUser));

      assertThrows(RuntimeException.class, () -> userService.getAllUsers("1"));

      verify(userRepository, never()).findAll();
    }
  }

  // =========================
  // DELETE USER
  // =========================

  @Nested
  class DeleteUser {

    @Test
    void shouldDeleteUserSuccessfully() {

      when(userMapper.toUser(userDTO)).thenReturn(user);

      userService.deleteUser(userDTO);

      verify(userRepository).delete(user);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenDeleteFails() {

      when(userMapper.toUser(userDTO)).thenReturn(user);
      doThrow(new RuntimeException()).when(userRepository).delete(user);

      assertThrows(RuntimeException.class, () -> userService.deleteUser(userDTO));
    }
  }

  // =========================
  // MANAGE LIFE EVENTS
  // =========================

  @Nested
  class ManageLifeEvents {

    @Test
    void addLifeEventToUser_shouldAddEventAndPersistUser() {

      Long eventId = 10L;
      LifeEvent event = new LifeEvent();
      PolicyRecommendation recommendation = new PolicyRecommendation();

      userDTO.setLifeEventIds(new java.util.HashSet<>());

      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenReturn(user);
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      when(lifeEventRepository.findById(eventId)).thenReturn(Optional.of(event));
      when(recommendationEngine.generateRecommendation(eq(user), eq(event), any()))
          .thenReturn(recommendation);
      when(policyRecommendationRepository.save(recommendation)).thenReturn(recommendation);

      UserDTO result = userService.addLifeEventToUser(userDTO, eventId);

      assertTrue(userDTO.getLifeEventIds().contains(eventId));
      assertEquals(userDTO, result);

      verify(policyRecommendationRepository).save(recommendation);
      verify(userRepository).save(user);
    }

    @Test
    void removeLifeEventForUser_shouldRemoveEventAndPersistUser() {

      Long eventId = 5L;
      LifeEvent event = new LifeEvent();
      PolicyRecommendation recommendation = new PolicyRecommendation();

      userDTO.setLifeEventIds(new java.util.HashSet<>(java.util.List.of(eventId)));

      when(userMapper.toUser(userDTO)).thenReturn(user);
      when(userRepository.save(user)).thenReturn(user);
      when(userMapper.toUserDTO(user)).thenReturn(userDTO);

      when(lifeEventRepository.findById(eventId)).thenReturn(Optional.of(event));
      when(recommendationEngine.generateRecommendation(eq(user), eq(event), any()))
          .thenReturn(recommendation);
      when(policyRecommendationRepository.save(recommendation)).thenReturn(recommendation);

      UserDTO result = userService.removeLifeEventForUser(userDTO, eventId);

      assertFalse(userDTO.getLifeEventIds().contains(eventId));
      assertEquals(userDTO, result);

      verify(policyRecommendationRepository).save(recommendation);
      verify(userRepository).save(user);
    }
  }

  // =========================
  // GET LATEST POLICY RECOMMENDATION
  // =========================

  @Nested
  class GetLatestPolicyRecommendation {

    @Test
    void shouldReturnLatestRecommendationWhenFound() {

      String userId = "1";
      PolicyRecommendation recommendation = new PolicyRecommendation();
      PolicyRecommendationDTO dto = new PolicyRecommendationDTO();

      when(policyRecommendationRepository.findTopByUserIdOrderByCreatedAtDesc(userId))
          .thenReturn(Optional.of(recommendation));

      when(policyRecommendationMapper.toDto(recommendation)).thenReturn(dto);

      PolicyRecommendationDTO result =
          userService.getLatestPolicyRecommendationForUser(userId);

      assertEquals(dto, result);
    }

    @Test
    void shouldThrowResourceNotFoundWhenRecommendationDoesNotExist() {

      String userId = "1";

      when(policyRecommendationRepository.findTopByUserIdOrderByCreatedAtDesc(userId))
          .thenReturn(Optional.empty());

      assertThrows(
          ResourceNotFoundException.class,
          () -> userService.getLatestPolicyRecommendationForUser(userId));
    }
  }

  // =========================
// ADDITIONAL COVERAGE (FIXED)
// =========================

@Nested
class AdditionalCoverage {

  @Test
  void addLifeEventToUser_shouldThrow_whenLifeEventNotFound() {

    Long eventId = 10L;

    when(lifeEventRepository.findById(eventId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
        () -> userService.addLifeEventToUser(userDTO, eventId));

    verify(lifeEventRepository).findById(eventId);
  }

  @Test
  void addPolicyToUser_shouldGenerateNewRecommendation_whenPreviousExists() {

    Long policyId = 1L;

    PolicyRecommendation existingRecommendation = new PolicyRecommendation();
    LifeEvent lifeEvent = new LifeEvent();
    existingRecommendation.setLifeEvent(lifeEvent);

    PolicyRecommendation newRecommendation = new PolicyRecommendation();

    // REQUIRED because updateUser() is called internally
    when(userMapper.toUser(userDTO)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toUserDTO(user)).thenReturn(userDTO);

    when(policyRecommendationRepository
        .findTopByUserIdOrderByCreatedAtDesc(userDTO.getId()))
        .thenReturn(Optional.of(existingRecommendation));

    when(recommendationEngine.generateRecommendation(eq(user), eq(lifeEvent), any()))
        .thenReturn(newRecommendation);

    UserDTO result = userService.addPolicyToUser(userDTO, policyId);

    assertEquals(userDTO, result);
    verify(policyRecommendationRepository).save(newRecommendation);
  }

  @Test
  void addPolicyToUser_shouldNotGenerateRecommendation_whenNoPreviousExists() {

    Long policyId = 1L;

    // REQUIRED because updateUser() is called internally
    when(userMapper.toUser(userDTO)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(userMapper.toUserDTO(user)).thenReturn(userDTO);

    when(policyRecommendationRepository
        .findTopByUserIdOrderByCreatedAtDesc(userDTO.getId()))
        .thenReturn(Optional.empty());

    UserDTO result = userService.addPolicyToUser(userDTO, policyId);

    assertEquals(userDTO, result);
    verify(policyRecommendationRepository, never()).save(any());
  }

  @Test
  void changeRoleOfUser_shouldUpdateRole_whenAdmin() {

    User admin = createUser();
    admin.setRole(UserRole.ADMIN);

    User targetUser = createUser();

    when(userRepository.findById("user1")).thenReturn(Optional.of(targetUser));
    when(userRepository.findById("admin1")).thenReturn(Optional.of(admin));
    when(userMapper.toUserDTO(targetUser)).thenReturn(userDTO);

    UserDTO result =
        userService.changeRoleOfUser("user1", "admin1", UserRole.POLICY_MANAGER);

    assertEquals(UserRole.POLICY_MANAGER, targetUser.getRole());
    assertEquals(userDTO, result);

    verify(userRepository).save(targetUser);
  }

  @Test
  void changeRoleOfUser_shouldThrow_whenAdminIsNotAdmin() {

    User notAdmin = createUser();
    notAdmin.setRole(UserRole.USER);

    User targetUser = createUser();

    when(userRepository.findById("user1")).thenReturn(Optional.of(targetUser));
    when(userRepository.findById("admin1")).thenReturn(Optional.of(notAdmin));

    assertThrows(RuntimeException.class,
        () -> userService.changeRoleOfUser("user1", "admin1", UserRole.ADMIN));
  }

  @Test
  void changeRoleOfUser_shouldThrow_whenUserNotFound() {

    when(userRepository.findById("user1")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class,
        () -> userService.changeRoleOfUser("user1", "admin1", UserRole.ADMIN));

    verify(userRepository).findById("user1");
  }

  @Test
  void getLatestPolicyRecommendation_shouldThrowRuntimeException_whenUnexpectedErrorOccurs() {

    String userId = "1";

    when(policyRecommendationRepository
        .findTopByUserIdOrderByCreatedAtDesc(userId))
        .thenThrow(new RuntimeException("db error"));

    assertThrows(RuntimeException.class,
        () -> userService.getLatestPolicyRecommendationForUser(userId));
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
    UserDTO dto = new UserDTO("1", "testuser");
    dto.setLifeEventIds(new java.util.HashSet<>());
    dto.setPolicyIds(new java.util.HashSet<>());
    return dto;
  }
}