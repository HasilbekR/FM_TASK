package com.vention.fm.repository.user;

import com.vention.fm.domain.model.user.User;

import java.util.List;
import java.util.UUID;


public interface UserRepository {
    String GET_QUERY = "select id as user_id, username, email, role, password, is_blocked as user_is_blocked from users ";

    String INSERT = "insert into users (id, created_date, updated_date, is_blocked, username, email, password, is_verified, role) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = GET_QUERY + " where id = ?";
    String GET_BY_USERNAME = GET_QUERY + " where username = ?";
    String GET_BY_EMAIL = GET_QUERY + " where email = ?";
    String GET_ALL_USERS = GET_QUERY + " where is_blocked = ?";
    String GET_USER_ROLE = "select role from users where id = ?";
    String IS_BLOCKED = "select is_blocked from users where id = ?";
    String BLOCK_USER = "update users set is_blocked = ? where username = ?";

    void save(User user);

    User getById(UUID userId);

    User getByUsername(String username);

    User getByEmail(String email);

    List<User> getAllUsers(Boolean isBlocked);

    String getUserRole(UUID userId);

    boolean isBlocked(UUID userId);

    void blockUser(Boolean isBlocked, String username);
}
