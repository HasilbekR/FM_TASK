package com.vention.fm.service;

import com.vention.fm.domain.dto.user.LoginDto;
import com.vention.fm.domain.dto.user.UserRequestDto;
import com.vention.fm.domain.model.user.UserEntity;
import com.vention.fm.domain.model.user.UserRole;
import com.vention.fm.exception.AuthenticationFailedException;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.exception.UniqueObjectException;
import com.vention.fm.repository.user.UserRepository;
import com.vention.fm.repository.user.UserRepositoryImpl;

import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();

    public UserEntity signIn(LoginDto loginDto) {
        UserEntity user = userRepository.getByUsername(loginDto.getUsername());
        if (user.getPassword().equals(loginDto.getPassword())) {
            return user;
        } else {
            throw new AuthenticationFailedException("Wrong password");
        }
    }

    public void signUp(UserRequestDto userRequestDto) {
        UserEntity userEntity = new UserEntity();
        //first checks for username if it exists using try catch,
        // if not the username is given in catch block as repository throws exception if data not found
        try {
            if (userRepository.getByUsername(userRequestDto.getUsername()) != null)
                throw new UniqueObjectException("Username already exists");
        } catch (DataNotFoundException e) {
            userEntity.setUsername(userRequestDto.getUsername());
        }
        //the same logic for email
        try {
            if (userRepository.getByEmail(userRequestDto.getEmail()) != null)
                throw new UniqueObjectException("Email already exists");
        } catch (DataNotFoundException e) {
            userEntity.setEmail(userRequestDto.getEmail());
        }
        if (userRequestDto.getPassword().isEmpty()) throw new DataNotFoundException("Password cannot be null");
        userEntity.setPassword(userRequestDto.getPassword());
        userEntity.setIsVerified(false);
        userEntity.setRole(UserRole.USER);
        userRepository.save(userEntity);
    }

    public String getUserRole(UUID userId) {
        return userRepository.getUserRole(userId);
    }

    public Boolean isBlocked(UUID userId) {
        return userRepository.isBlocked(userId);
    }

    public void blockUser(Boolean isBlocked, UUID userId) {
        userRepository.blockUser(isBlocked, userId);
    }

    public List<UserEntity> getAllActiveUsers() {
        return userRepository.getAllUsers(false);
    }

    public List<UserEntity> getAllBlockedUsers() {
        return userRepository.getAllUsers(true);
    }
}
