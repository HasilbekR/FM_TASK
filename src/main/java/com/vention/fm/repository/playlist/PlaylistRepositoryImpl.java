package com.vention.fm.repository.playlist;

import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.utils.DatabaseUtils;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistRepositoryImpl implements PlaylistRepository {
    private final Connection connection = Utils.getConnection();
    private static final Logger log = LoggerFactory.getLogger(PlaylistRepositoryImpl.class);

    @Override
    public void save(Playlist playlist) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, playlist);
            preparedStatement.setString(5, playlist.getName());
            preparedStatement.setBoolean(6, playlist.getIsPublic());
            preparedStatement.setInt(7, playlist.getLikeCount());
            preparedStatement.setInt(8, playlist.getDislikeCount());
            preparedStatement.setObject(9, playlist.getOwner().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while saving playlist", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Playlist getById(UUID id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapPlaylist(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving playlist", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Playlist getPlaylistByName(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapPlaylist(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving playlist", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Playlist getPlaylistState(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_PLAYLIST_STATE);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapPlaylistState(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving playlist state", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<Playlist> getAllByOwnerId(UUID ownerId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_OWNER_ID);
            preparedStatement.setObject(1, ownerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Playlist> playlists = new ArrayList<>();
            while (resultSet.next()) {
                playlists.add(ResultSetMapper.mapPlaylist(resultSet));
            }
            return playlists;
        } catch (SQLException e) {
            log.error("Error occurred while retrieving playlists", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<Playlist> getAvailablePlaylists() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_AVAILABLE_PLAYLISTS);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Playlist> playlists = new ArrayList<>();
            while (resultSet.next()) {
                playlists.add(ResultSetMapper.mapPlaylist(resultSet));
            }
            return playlists;
        } catch (SQLException e) {
            log.error("Error occurred while retrieving available playlists", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void update(Playlist playlist) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setObject(1, playlist.getUpdatedDate());
            preparedStatement.setString(2, playlist.getName());
            preparedStatement.setBoolean(3, playlist.getIsPublic());
            preparedStatement.setObject(4, playlist.getOwner().getId());
            preparedStatement.setObject(5, playlist.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while updating playlist", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void rate(int likeCount, int dislikeCount, UUID playlistId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(RATE);
            preparedStatement.setObject(1, LocalDateTime.now());
            preparedStatement.setInt(2, likeCount);
            preparedStatement.setInt(3, dislikeCount);
            preparedStatement.setObject(4, playlistId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while rating playlist", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while deleting playlist", e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
