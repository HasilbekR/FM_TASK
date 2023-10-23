package com.vention.fm.repository.playlist_tracks;

import com.vention.fm.domain.model.playlist.PlaylistTracks;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.utils.DatabaseUtils;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaylistTracksRepositoryImpl implements PlaylistTracksRepository {
    private final Connection connection = Utils.getConnection();

    @Override
    public void save(PlaylistTracks playlistTracks) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, playlistTracks);
            preparedStatement.setObject(5, playlistTracks.getPlaylistId());
            preparedStatement.setObject(6, playlistTracks.getTrackId());
            preparedStatement.setInt(7, playlistTracks.getTrackPosition());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UUID playlistId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setObject(1, playlistId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PlaylistTracks getById(UUID playlistTrackId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
            preparedStatement.setObject(1, playlistTrackId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return ResultSetMapper.mapPlaylistTracks(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Playlist track not found");
    }

    @Override
    public int countPlaylistTracks(UUID playlistId) {
        return DatabaseUtils.getData(playlistId, connection, COUNT_PLAYLIST_TRACKS);
    }

    @Override
    public void removeTrack(UUID playlistTrackId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_TRACK);
            preparedStatement.setObject(1, playlistTrackId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<PlaylistTracks> getPlaylistTracks(UUID playlistId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_PLAYLIST_TRACKS);
            preparedStatement.setObject(1, playlistId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<PlaylistTracks> playlistTracksList = new ArrayList<>();
            while (resultSet.next()) {
                playlistTracksList.add(ResultSetMapper.mapPlaylistTracks(resultSet));
            }
            return playlistTracksList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
