package com.vention.fm.utils;

import com.vention.fm.domain.model.BaseModel;
import com.vention.fm.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class DatabaseUtils {
    private static final Logger log = LoggerFactory.getLogger(DatabaseUtils.class);

    public static void setValues(PreparedStatement preparedStatement, BaseModel baseModel) throws SQLException {
        preparedStatement.setObject(1, baseModel.getId());
        preparedStatement.setTimestamp(2, Timestamp.valueOf(baseModel.getCreatedDate()));
        preparedStatement.setTimestamp(3, Timestamp.valueOf(baseModel.getUpdatedDate()));
        preparedStatement.setBoolean(4, baseModel.getIsBlocked());
    }

    public static boolean isBlocked(UUID userId, Connection connection, String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            } else {
                // if the object not found returning true means it is blocked, it ensures no further processing
                return true;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving data from database", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    public static Integer getPerformanceData(UUID id, Connection connection, String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving data from database", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    public static void updateTrackPosition(UUID id, int position, Connection connection, String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, LocalDateTime.now());
            preparedStatement.setInt(2, position);
            preparedStatement.setObject(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while retrieving data from database", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    public static void removeTrack(UUID id, UUID trackId, Connection connection, String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, id);
            preparedStatement.setObject(2, trackId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while retrieving data from database", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    public static void block(Boolean isBlocked, String name, Connection connection, String query) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, isBlocked);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while retrieving data from database", e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
