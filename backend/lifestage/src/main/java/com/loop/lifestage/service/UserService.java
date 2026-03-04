
package com.loop.lifestage.service;

import com.loop.lifestage.model.User;
import com.loop.lifestage.dto.UserDTO;
import com.loop.lifestage.mapper.UserMapper;
import com.loop.lifestage.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;



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
        User user = userMapper.toUser(userDTO);
        User savedUser = userRepository.save(user);
        return userMapper.toUserDTO(savedUser);
    }

    @Transactional
    public UserDTO updateUser(UserDTO userDTO) {
        User user = userMapper.toUser(userDTO);
        User updatedUser = userRepository.save(user);
        return userMapper.toUserDTO(updatedUser);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapper.toUserDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteUser(UserDTO userDTO){
        User user = userMapper.toUser(userDTO);
        userRepository.delete(user);
    }
}
