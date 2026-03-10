package com.loop.lifestage.service;

import com.loop.lifestage.dto.UserDTO;
import com.loop.lifestage.exception.BadRequestException;
import com.loop.lifestage.exception.ResourceAlreadyExistsException;
import com.loop.lifestage.exception.ResourceNotFoundException;
import com.loop.lifestage.mapper.UserMapper;
import com.loop.lifestage.model.user.User;
import com.loop.lifestage.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Transactional
  public UserDTO createUser(UserDTO userDTO) {
    try {
      User user = userMapper.toUser(userDTO);
      User savedUser = userRepository.save(user);
      return userMapper.toUserDTO(savedUser);
    } catch (DataIntegrityViolationException e) {
      throw new ResourceAlreadyExistsException("User with credentails already exists");
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
      return userMapper.toUserDTO(user);
    } catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("User with id " + id + " not found");
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while retrieving the user", e);
    }
  }

  @Transactional(readOnly = true)
  public List<UserDTO> getAllUsers() {
    try {
      List<User> users = userRepository.findAll();
      return users.stream().map(userMapper::toUserDTO).collect(Collectors.toList());
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while retrieving users", e);
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
      return updateUser(userDTO);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while adding life event to the user", e);
    }
  }

  @Transactional
  public UserDTO removeLifeEventForUser(UserDTO userDTO, Long lifeEventId) {
    try {
      userDTO.removeLifeEvent(lifeEventId);
      return updateUser(userDTO);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while removing life event from the user", e);
    }
  }

  @Transactional
  public UserDTO addPolicyToUser(UserDTO userDTO, Long policyId) {
    try {
      userDTO.addPolicyById(policyId);
      return updateUser(userDTO);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while adding policy to the user", e);
    }
  }

  @Transactional
  public UserDTO removePolicyForUser(UserDTO userDTO, Long policyId) {
    try {
      userDTO.removePolicyById(policyId);
      return updateUser(userDTO);
    } catch (Exception e) {
      throw new RuntimeException("An error occurred while removing policy from the user", e);
    }
  }
}
