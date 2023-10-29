package com.vention.fm.repository.user;

import com.vention.fm.domain.model.user.User;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.utils.DatabaseUtils;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class UserRepositoryImpl implements UserRepository {
    private final Connection connection = Utils.getConnection();
    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public void save(User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, user);
            preparedStatement.setString(5, user.getUsername());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getPassword());
            preparedStatement.setBoolean(8, user.getIsVerified());
            preparedStatement.setObject(9, user.getRole(), Types.OTHER);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while saving user", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public User getById(UUID userId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
            preparedStatement.setObject(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapUser(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving user", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public User getByUsername(String username) {
        return getUserEntity(username, GET_BY_USERNAME);
    }

    @Override
    public User getByEmail(String email) {
        return getUserEntity(email, GET_BY_EMAIL);
    }

    @Override
    public List<User> getAllUsers(Boolean isBlocked) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USERS);
            preparedStatement.setObject(1, isBlocked);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new LinkedList<>();
            while (resultSet.next()) {
                users.add(ResultSetMapper.mapUser(resultSet));
            }
            return users;
        } catch (SQLException e) {
            log.error("Error occurred while retrieving users", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public String getUserRole(UUID userId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_ROLE);
            preparedStatement.setObject(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(1);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving user role", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public boolean isBlocked(UUID userId) {
        return DatabaseUtils.isBlocked(userId, connection, IS_BLOCKED);
    }

    @Override
    public void blockUser(Boolean isBlocked, String username) {
        DatabaseUtils.block(isBlocked, username, connection, BLOCK_USER);
    }

    private User getUserEntity(String email, String getByEmail) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(getByEmail);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapUser(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving user", e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
