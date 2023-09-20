package org.example.repository;

import org.example.domain.model.UserEntity;
import org.example.utils.BeanUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepositoryImpl implements UserRepository{
    private final Connection connection = BeanUtil.getConnection();
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
                return UserEntity.map(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void save(UserEntity userEntity) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setObject(1, userEntity.getId());
            preparedStatement.setObject(2, userEntity.getCreatedDate());
            preparedStatement.setObject(3, userEntity.getUpdatedDate());
            preparedStatement.setString(4, userEntity.getUsername());
            preparedStatement.setString(5, userEntity.getEmail());
            preparedStatement.setString(6, userEntity.getPassword());
            preparedStatement.setBoolean(7, userEntity.getIsVerified());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
