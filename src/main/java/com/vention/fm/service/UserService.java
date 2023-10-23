package com.vention.fm.service;

import com.vention.fm.domain.dto.user.LoginDto;
import com.vention.fm.domain.dto.user.UserDto;
import com.vention.fm.domain.dto.user.UserRequestDto;
import com.vention.fm.domain.model.user.User;
import com.vention.fm.domain.model.user.UserRole;
import com.vention.fm.exception.AuthenticationFailedException;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.exception.UniqueObjectException;
import com.vention.fm.repository.user.UserRepository;
import com.vention.fm.repository.user.UserRepositoryImpl;
import com.vention.fm.utils.Utils;
import org.modelmapper.ModelMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final ModelMapper modelMapper = Utils.modelMapper();

    public void signUp(UserRequestDto userRequestDto) {
        if (userRepository.getByUsername(userRequestDto.getUsername()) != null) {
            throw new UniqueObjectException("Username already exists");
        }
        if (userRepository.getByEmail(userRequestDto.getEmail()) != null) {
            throw new UniqueObjectException("Email already exists");
        }
        if (userRequestDto.getPassword().isEmpty()) {
            throw new DataNotFoundException("Password cannot be empty");
        }
        User user = new User(userRequestDto.getUsername(), userRequestDto.getEmail(), userRequestDto.getPassword(), false, UserRole.USER);
        userRepository.save(user);
    }

    public UserDto signIn(LoginDto loginDto) {
        User user = userRepository.getByUsername(loginDto.getUsername());
        if (user != null) {
            if (user.getPassword().equals(loginDto.getPassword())) {
                return modelMapper.map(user, UserDto.class);
            } else {
                throw new AuthenticationFailedException("Wrong password");
            }
        } else {
            throw new AuthenticationFailedException("Invalid username");
        }
    }

    public User getById(UUID userId) {
        return userRepository.getById(userId);
    }

    public User getUserState(UUID userId) {
        boolean isBlocked = userRepository.isBlocked(userId);
        User user = new User();
        user.setId(userId);
        user.setIsBlocked(isBlocked);
        return user;
    }

    public List<UserDto> getAllActiveUsers() {
        List<User> allUsers = userRepository.getAllUsers(false);
        return mapToDtoList(allUsers);
    }

    public List<UserDto> getAllBlockedUsers() {
        List<User> allUsers = userRepository.getAllUsers(true);
        return mapToDtoList(allUsers);
    }

    public void doesUserExist(UUID userId) {
        String userRole = userRepository.getUserRole(userId);
        if (userRole == null) {
            throw new DataNotFoundException("User not found");
        }
    }

    public String getUserRole(UUID userId) {
        String userRole = userRepository.getUserRole(userId);
        if (userRole != null) {
            return userRole;
        } else {
            throw new DataNotFoundException("User not found");
        }
    }

    public boolean isBlocked(UUID userId) {
        return userRepository.isBlocked(userId);
    }

    public void blockUser(Boolean isBlocked, String username) {
        User user = userRepository.getByUsername(username);
        if (user != null) {
            userRepository.blockUser(isBlocked, username);
        } else {
            throw new DataNotFoundException("User with username " + username + " not found");
        }
    }

    private List<UserDto> mapToDtoList(List<User> users) {
        List<UserDto> userDtoList = new LinkedList<>();
        for (User user : users) {
            userDtoList.add(modelMapper.map(user, UserDto.class));
        }
        return userDtoList;
    }
}
