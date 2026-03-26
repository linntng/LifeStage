package com.loop.lifestage.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PolicyRecommendationEngine recommendationEngine;
  private final LifeEventRepository lifeEventRepository;
  private final PolicyRecommendationRepository policyRecommendationRepository;
  private final PolicyRecommendationMapper policyRecommendationMapper;
  private final AuditService auditService;

  public UserService(
    UserRepository userRepository, 
    UserMapper userMapper,
    PolicyRecommendationEngine recommendationEngine,
    LifeEventRepository lifeEventRepository,
    PolicyRecommendationRepository policyRecommendationRepository,
    PolicyRecommendationMapper policyRecommendationMapper,
    AuditService auditService) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
    this.policyRecommendationMapper = policyRecommendationMapper;
    this.recommendationEngine = recommendationEngine;
    this.lifeEventRepository = lifeEventRepository;
    this.policyRecommendationRepository = policyRecommendationRepository;
    this.auditService = auditService;
  }

  @Transactional
  public UserDTO createUser(UserDTO userDTO) {
    try {
      User user = userMapper.toUser(userDTO);
      if (user.getRole() == null) {
        user.setRole(UserRole.USER);
      }
      User savedUser = userRepository.save(user);
      return userMapper.toUserDTO(savedUser);
    } catch (DataIntegrityViolationException e) {
      throw new ResourceAlreadyExistsException("User with credentials already exists");
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid user data: " + e.getMessage());
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while creating the user", e);
    }
  }

  @Transactional
  public UserDTO updateUser(UserDTO userDTO) {
    try {
      User user = userMapper.toUser(userDTO);
      User updatedUser = userRepository.save(user);
      return userMapper.toUserDTO(updatedUser);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid user data: " + e.getMessage());
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("User not found with id: " + userDTO.getId());
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while updating the user", e);
    }
  }

  @Transactional(readOnly = true)
  public UserDTO getUserById(String id) {
    try {
      User user =
          userRepository
              .findById(id)
              .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

               // 1. Logg at selve brukerprofilen ble lest
    auditService.logAccess("USER_READ", id);
      

      if (user.getLifeEvents() != null) {
        user.getLifeEvents().forEach(le -> 
            auditService.logAccess("LIFEEVENT_READ", le.getId().toString())
        );
    }

     // 3. Logg innsyn i alle Policies
    if (user.getPolicies() != null) {
        user.getPolicies().forEach(p -> 
            auditService.logAccess("POLICY_READ", p.getId().toString())
        );
    }

    return userMapper.toUserDTO(user);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("User with id " + id + " not found");
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while retrieving the user", e);
    }
  }

  @Transactional(readOnly = true)
  public List<UserDTO> getAllUsers(String id) {
    try {
      User user =
          userRepository
              .findById(id)
              .orElseThrow(() -> new RuntimeException("Admin not found"));
      if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.CASE_HANDLER) {
        List<User> users = userRepository.findAll();
        return users.stream().map(userMapper::toUserDTO).collect(Collectors.toList());
      } else {
        throw new RuntimeException("Only admins can access all users");
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

  @Transactional
  public void deleteUser(UserDTO userDTO) {
    try {
      User user = userMapper.toUser(userDTO);
      userRepository.delete(user);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while deleting the user", e);
    }
  }

  @Transactional
  public UserDTO addLifeEventToUser(UserDTO userDTO, Long lifeEventId) {
    try {
      userDTO.addLifeEvent(lifeEventId);
      User user = userMapper.toUser(userDTO);
      LifeEvent event = lifeEventRepository.findById(lifeEventId).orElseThrow(() -> new RuntimeException());
      PolicyRecommendation recommendation = recommendationEngine.generateRecommendation(user, event, new PolicyRecommendation());
      policyRecommendationRepository.save(recommendation);
      return updateUser(userDTO);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while adding life event to the user", e);
    }
  }

  @Transactional
  public UserDTO removeLifeEventForUser(UserDTO userDTO, Long lifeEventId) {
    try {
      userDTO.removeLifeEvent(lifeEventId);
      User user = userMapper.toUser(userDTO);
      LifeEvent event = lifeEventRepository.findById(lifeEventId).orElseThrow(() -> new RuntimeException());
      PolicyRecommendation recommendation = recommendationEngine.generateRecommendation(user, event, new PolicyRecommendation());
      policyRecommendationRepository.save(recommendation);
      return updateUser(userDTO);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while removing life event from the user", e);
    }
  }

  @Transactional
  public UserDTO addPolicyToUser(UserDTO userDTO, Long policyId, String callerId) {
    try {
      userDTO.addPolicyById(policyId);
      User user = userRepository
          .findById(callerId)
          .orElseThrow(() -> new RuntimeException("Case handler not found"));
      if (user.getRole() != UserRole.CASE_HANDLER && user.getRole() != UserRole.ADMIN) {
        throw new RuntimeException("Only case handlers and admins can add policies to users");
      }
      
      PolicyRecommendation latestRecommendation =
        policyRecommendationRepository
            .findTopByUserIdOrderByCreatedAtDesc(userDTO.getId())
            .orElse(null);
      if (latestRecommendation != null) {
        policyRecommendationRepository
          .save(recommendationEngine.generateRecommendation(user, latestRecommendation.getLifeEvent(), new PolicyRecommendation()));
      }
      return updateUser(userDTO);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while adding policy to the user", e);
    }
  }

  @Transactional
  public UserDTO removePolicyForUser(UserDTO userDTO, Long policyId) {
    try {
      userDTO.removePolicyById(policyId);
      User user = userMapper.toUser(userDTO);
      PolicyRecommendation latestRecommendation =
        policyRecommendationRepository
            .findTopByUserIdOrderByCreatedAtDesc(userDTO.getId())
            .orElse(null);
      if (latestRecommendation != null) {
        policyRecommendationRepository
          .save(recommendationEngine.generateRecommendation(user, latestRecommendation.getLifeEvent(), new PolicyRecommendation()));
      }
      return updateUser(userDTO);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while removing policy from the user", e);
    }
  }

  @Transactional
  public UserDTO changeRoleOfUser(String userId, String adminId, UserRole role) {
    try {
      User user =
          userRepository
              .findById(userId)
              .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
      User admin =
          userRepository
              .findById(adminId)
              .orElseThrow(
                  () -> new EntityNotFoundException("Admin not found with id: " + adminId));
      if (admin.getRole() == UserRole.ADMIN) {
        user.setRole(role);
        userRepository.save(user);
        return userMapper.toUserDTO(user);
      } else {
        throw new RuntimeException("Could not change role of user: " + userId);
      }
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage(), e);
    }
  }

   @Transactional(readOnly = true)
  public PolicyRecommendationDTO getLatestPolicyRecommendationForUser(String userId) {
    try {

      PolicyRecommendation recommendation =
          policyRecommendationRepository
              .findTopByUserIdOrderByCreatedAtDesc(userId)
              .orElseThrow(
                  () ->
                      new ResourceNotFoundException(
                          "No policy recommendations found for user id: " + userId));

      return policyRecommendationMapper.toDto(recommendation);

    } catch (ResourceNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(
          "An error occurred while fetching the latest policy recommendation", e);
    }
  }
}
