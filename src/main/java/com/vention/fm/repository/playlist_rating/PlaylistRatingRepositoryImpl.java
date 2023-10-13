package com.vention.fm.repository.playlist_rating;

import com.vention.fm.domain.model.playlist.PlaylistRating;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.utils.DatabaseUtils;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

public class PlaylistRatingRepositoryImpl implements PlaylistRatingRepository {
    private final Connection connection = Utils.getConnection();

    @Override
    public void save(PlaylistRating playlistRating) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, playlistRating);
            preparedStatement.setObject(5, playlistRating.getPlaylistId());
            preparedStatement.setObject(6, playlistRating.getUserId());
            preparedStatement.setBoolean(7, playlistRating.getIsLiked());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(UUID playlistId, UUID userId, Boolean isLiked) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setObject(1, LocalDateTime.now());
            preparedStatement.setBoolean(2, isLiked);
            preparedStatement.setObject(3, playlistId);
            preparedStatement.setObject(4, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UUID playlistId, UUID userId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setObject(1, playlistId);
            preparedStatement.setObject(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlaylistRating get(UUID playlistId, UUID userId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET);
            preparedStatement.setObject(1, playlistId);
            preparedStatement.setObject(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapPlaylistRating(resultSet);
            }
            throw new DataNotFoundException("Playlist rating not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getLikeCount(UUID playlistId) {
        return DatabaseUtils.getData(playlistId, connection, GET_LIKE_COUNT);
    }

    @Override
    public int getDislikeCount(UUID playlistId) {
        return DatabaseUtils.getData(playlistId, connection, GET_DISLIKE_COUNT);
    }
}
