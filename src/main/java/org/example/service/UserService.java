package org.example.service;

import org.example.domain.dto.LoginDto;
import org.example.domain.dto.UserRequestDto;
import org.example.domain.model.user.UserEntity;
import org.example.domain.model.user.UserRole;
import org.example.exception.AuthenticationFailedException;
import org.example.exception.UniqueObjectException;
import org.example.repository.UserRepository;
import org.example.repository.UserRepositoryImpl;

public class UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();
    public UserEntity signIn(LoginDto loginDto) {
        UserEntity user = userRepository.getByUsername(loginDto.getUsername());
        if(user != null){
            if(user.getPassword().equals(loginDto.getPassword())){
                return user;
            }else{
                throw new AuthenticationFailedException("Wrong password");
            }
        }
        throw new AuthenticationFailedException("Invalid username");
    }

    public void signUp(UserRequestDto userRequestDto) {
        if(userRepository.getByUsername(userRequestDto.getUsername()) != null) throw new UniqueObjectException("Username already exists");
        if(userRepository.getByEmail(userRequestDto.getEmail()) != null) throw new UniqueObjectException("Email already exists");
        UserEntity userEntity = new UserEntity(userRequestDto.getUsername(), userRequestDto.getEmail(), userRequestDto.getPassword(), false, UserRole.USER);
        userRepository.save(userEntity);
    }
}
