package com.vention.fm.utils;

import com.vention.fm.domain.model.BaseModel;
import com.vention.fm.exception.DataNotFoundException;

import java.sql.*;
import java.util.UUID;

public class DatabaseUtils {
    public static void setValues(PreparedStatement preparedStatement, BaseModel baseModel) throws SQLException {
        preparedStatement.setObject(1, baseModel.getId());
        preparedStatement.setObject(2, baseModel.getCreatedDate());
        preparedStatement.setObject(3, baseModel.getUpdatedDate());
        preparedStatement.setBoolean(4, baseModel.getIsBlocked());
    }

    public static Boolean isBlocked(UUID userId, Connection connection, String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Data not found");
    }

    public static Integer getData(UUID id, Connection connection, String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Data not found");
    }

    public static void block(Boolean isBlocked, UUID id, Connection connection, String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, isBlocked);
            preparedStatement.setObject(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
