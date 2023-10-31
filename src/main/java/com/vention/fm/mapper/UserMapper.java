package com.vention.fm.mapper;

import com.vention.fm.domain.dto.user.UserResponseDto;
import com.vention.fm.domain.model.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    UserResponseDto userToDto(User user);
    List<UserResponseDto> usersToDto(List<User> users);
}
