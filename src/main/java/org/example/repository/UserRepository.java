package org.example.repository;

import org.example.domain.model.user.UserEntity;


public interface UserRepository {
    String GET_BY_USERNAME = "select * from users where username = ?";
    String GET_BY_EMAIL = "select * from users where email = ?";
    String INSERT = "insert into users (id, createddate, updateddate, username, email, password, isverified) values(?, ?, ?, ?, ?, ?, ?)";
    UserEntity getByUsername(String username);
    UserEntity getByEmail(String email);
    void save(UserEntity userEntity);

}
