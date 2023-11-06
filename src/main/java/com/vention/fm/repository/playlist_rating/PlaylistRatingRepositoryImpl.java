package com.vention.fm.repository.playlist_rating;

import com.vention.fm.domain.model.playlist.PlaylistRating;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.utils.DatabaseUtils;
import com.vention.fm.utils.Utils;
import com.vention.fm.mapper.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class PlaylistRatingRepositoryImpl implements PlaylistRatingRepository {
    private final Connection connection = Utils.getConnection();
    private static final Logger log = LoggerFactory.getLogger(PlaylistRatingRepositoryImpl.class);
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    @Override
    public void save(PlaylistRating playlistRating) {
        try {
            preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, playlistRating);
            preparedStatement.setObject(5, playlistRating.getPlaylist().getId());
            preparedStatement.setObject(6, playlistRating.getUser().getId());
            preparedStatement.setBoolean(7, playlistRating.isLiked());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while saving rating playlist", e);
            throw new BadRequestException(e.getMessage());
        } finally {
            DatabaseUtils.close(resultSet, preparedStatement);
        }
    }

    @Override
    public PlaylistRating get(UUID playlistId, UUID userId) {
        try {
            preparedStatement = connection.prepareStatement(GET);
            preparedStatement.setObject(1, playlistId);
            preparedStatement.setObject(2, userId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapPlaylistRating(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving playlist rating", e);
            throw new BadRequestException(e.getMessage());
        } finally {
            DatabaseUtils.close(resultSet, preparedStatement);
        }
    }

    @Override
    public int getLikeCount(UUID playlistId) {
        return DatabaseUtils.getPerformanceData(playlistId, connection, GET_LIKE_COUNT);
    }

    @Override
    public int getDislikeCount(UUID playlistId) {
        return DatabaseUtils.getPerformanceData(playlistId, connection, GET_DISLIKE_COUNT);
    }

    @Override
    public void update(UUID playlistId, UUID userId, Boolean isLiked) {
        try {
            preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setObject(1, LocalDateTime.now());
            preparedStatement.setBoolean(2, isLiked);
            preparedStatement.setObject(3, playlistId);
            preparedStatement.setObject(4, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while updating playlist rating", e);
            throw new BadRequestException(e.getMessage());
        } finally {
            DatabaseUtils.close(resultSet, preparedStatement);
        }
    }

    @Override
    public void delete(UUID playlistId, UUID userId) {
        try {
            preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setObject(1, playlistId);
            preparedStatement.setObject(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while deleting playlist rating", e);
            throw new BadRequestException(e.getMessage());
        } finally {
            DatabaseUtils.close(resultSet, preparedStatement);
        }
    }
}
