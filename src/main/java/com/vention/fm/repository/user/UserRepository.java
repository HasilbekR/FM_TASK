package com.vention.fm.repository.user;

import com.vention.fm.domain.model.user.UserEntity;

import java.util.List;
import java.util.UUID;


public interface UserRepository {
    String GET_BY_USERNAME = "select * from users where username = ?";
    String GET_BY_EMAIL = "select * from users where email = ?";
    String INSERT = "insert into users (id, created_date, updated_date, is_blocked, username, email, password, is_verified, role) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String GET_USER_ROLE = "select role from users where id = ?";
    String IS_BLOCKED = "select is_blocked from users where id =?";
    String BLOCK_USER = "update users set is_blocked = ? where id =?";
    String GET_ALL_USERS = "select * from users where is_blocked = ?";

    UserEntity getByUsername(String username);

    UserEntity getByEmail(String email);

    void save(UserEntity userEntity);

    String getUserRole(UUID userId);

    Boolean isBlocked(UUID userId);

    void blockUser(Boolean isBlocked, UUID userId);

    List<UserEntity> getAllUsers(Boolean isBlocked);
}
