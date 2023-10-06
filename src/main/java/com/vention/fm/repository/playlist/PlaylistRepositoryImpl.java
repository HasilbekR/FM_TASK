package com.vention.fm.repository.playlist;

import com.vention.fm.domain.model.playlist.Playlist;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistRepositoryImpl implements PlaylistRepository{
    private final Connection connection = Utils.getConnection();
    @Override
    public Playlist getPlaylistByName(String name) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return ResultSetMapper.mapPlaylist(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Playlist with name " + name + " not found");
    }

    @Override
    public Playlist getPlaylistById(UUID id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return ResultSetMapper.mapPlaylist(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Playlist not found");
    }

    @Override
    public List<Playlist> getAvailablePlaylists() {
        try{
        PreparedStatement preparedStatement = connection.prepareStatement(GET_AVAILABLE_PLAYLISTS);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Playlist> playlists = new ArrayList<>();
            while (resultSet.next()){
                playlists.add(ResultSetMapper.mapPlaylist(resultSet));
            }
            return playlists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Playlist playlist) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setObject(1, playlist.getId());
            preparedStatement.setObject(2, playlist.getCreatedDate());
            preparedStatement.setObject(3, playlist.getUpdatedDate());
            preparedStatement.setBoolean(4, playlist.getIsBlocked());
            preparedStatement.setString(5, playlist.getName());
            preparedStatement.setBoolean(6, playlist.getIsPublic());
            preparedStatement.setInt(7, playlist.getLikeCount());
            preparedStatement.setInt(8, playlist.getDislikeCount());
            preparedStatement.setObject(9, playlist.getOwnerId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UUID id) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setObject(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void rate(int likeCount, int dislikeCount, UUID playlistId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(RATE);
            preparedStatement.setObject(1, LocalDateTime.now());
            preparedStatement.setInt(2, likeCount);
            preparedStatement.setInt(3, dislikeCount);
            preparedStatement.setObject(4, playlistId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Playlist> getAllByOwnerId(UUID ownerId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_OWNER_ID);
            preparedStatement.setObject(1, ownerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Playlist> playlists = new ArrayList<>();
            while (resultSet.next()){
                playlists.add(ResultSetMapper.mapPlaylist(resultSet));
            }
            return playlists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Playlist playlist) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setObject(1, playlist.getUpdatedDate());
            preparedStatement.setString(2, playlist.getName());
            preparedStatement.setBoolean(3, playlist.getIsPublic());
            preparedStatement.setObject(4, playlist.getOwnerId());
            preparedStatement.setObject(5, playlist.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
