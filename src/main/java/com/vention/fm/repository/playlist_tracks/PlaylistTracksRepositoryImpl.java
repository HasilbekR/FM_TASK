package com.vention.fm.repository.playlist_tracks;

import com.vention.fm.domain.model.playlist.PlaylistTracks;
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
            preparedStatement.setObject(5, playlistTracks.getPlaylist().getId());
            preparedStatement.setObject(6, playlistTracks.getTrack().getId());
            preparedStatement.setInt(7, playlistTracks.getTrackPosition());
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
            if (resultSet.next()) {
                return ResultSetMapper.mapPlaylistTracks(resultSet);
            } else {
                return null;
            }
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

    @Override
    public List<PlaylistTracks> getPlaylistTracksToReorder(UUID playlistId, UUID trackId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TRACKS_FOR_REORDER);
            preparedStatement.setObject(1, playlistId);
            preparedStatement.setObject(2, trackId);
            preparedStatement.setObject(3, playlistId);
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

    @Override
    public int countPlaylistTracks(UUID playlistId) {
        return DatabaseUtils.getPerformanceData(playlistId, connection, COUNT_PLAYLIST_TRACKS);
    }

    @Override
    public void updatePosition(UUID id, int trackPosition) {
        DatabaseUtils.updateTrackPosition(id, trackPosition, connection, UPDATE_POSITION);
    }

    @Override
    public void removeTrack(UUID playlistId, UUID trackId) {
        DatabaseUtils.removeTrack(playlistId, trackId, connection, REMOVE_TRACK);
    }

}
