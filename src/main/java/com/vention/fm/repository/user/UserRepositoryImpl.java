package com.vention.fm.repository.user;

import com.vention.fm.domain.model.user.UserEntity;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection = Utils.getConnection();
    @Override
    public UserEntity getByUsername(String username) {
        return getUserEntity(username, GET_BY_USERNAME);
    }

    @Override
    public UserEntity getByEmail(String email) {
        return getUserEntity(email, GET_BY_EMAIL);
    }

    private UserEntity getUserEntity(String email, String getByEmail) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getByEmail);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return ResultSetMapper.mapUser(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Invalid username or email");
    }

    @Override
    public void save(UserEntity userEntity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setObject(1, userEntity.getId());
            preparedStatement.setObject(2, userEntity.getCreatedDate());
            preparedStatement.setObject(3, userEntity.getUpdatedDate());
            preparedStatement.setBoolean(4,userEntity.getIsBlocked());
            preparedStatement.setString(5, userEntity.getUsername());
            preparedStatement.setString(6, userEntity.getEmail());
            preparedStatement.setString(7, userEntity.getPassword());
            preparedStatement.setBoolean(8, userEntity.getIsVerified());
            preparedStatement.setObject(9, userEntity.getRole(), Types.OTHER);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getUserRole(UUID userId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ROLE);
            preparedStatement.setObject(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("User not found");
    }

    @Override
    public Boolean isBlocked(UUID userId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(IS_BLOCKED);
            preparedStatement.setObject(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("User not found");
    }

    @Override
    public void blockUser(Boolean isBlocked, UUID userId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(BLOCK_USER);
            preparedStatement.setObject(1, isBlocked);
            preparedStatement.setObject(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserEntity> getAllUsers(Boolean isBlocked) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS);
            preparedStatement.setObject(1, isBlocked);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<UserEntity> users = new LinkedList<>();
            while (resultSet.next()) {
                users.add(ResultSetMapper.mapUser(resultSet));
            }
            return users;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
