package com.vention.fm.repository.album_tracks;

import com.vention.fm.domain.model.album.AlbumTracks;
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
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AlbumTracksRepositoryImpl implements AlbumTracksRepository {
    private final Connection connection = Utils.getConnection();
    private static final Logger log = LoggerFactory.getLogger(AlbumTracksRepositoryImpl.class);

    @Override
    public void save(AlbumTracks albumTracks) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, albumTracks);
            preparedStatement.setObject(5, albumTracks.getAlbum().getId());
            preparedStatement.setObject(6, albumTracks.getTrack().getId());
            preparedStatement.setInt(7, albumTracks.getTrackPosition());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while saving track to album", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<AlbumTracks> getAlbumTracks(UUID albumId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALBUM_TRACKS);
            preparedStatement.setObject(1, albumId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<AlbumTracks> albumTracks = new LinkedList<>();
            while (resultSet.next()) {
                albumTracks.add(ResultSetMapper.mapAlbumTracks(resultSet));
            }
            return albumTracks;
        } catch (SQLException e) {
            log.error("Error occurred while retrieving album tracks", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<AlbumTracks> getAlbumTracksToReorder(UUID albumId, UUID trackId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TRACKS_FOR_REORDER);
            preparedStatement.setObject(1, albumId);
            preparedStatement.setObject(2, trackId);
            preparedStatement.setObject(3, albumId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<AlbumTracks> albumTracksList = new LinkedList<>();
            while (resultSet.next()) {
                albumTracksList.add(ResultSetMapper.mapAlbumTracks(resultSet));
            }
            return albumTracksList;
        } catch (SQLException e) {
            log.error("Error occurred while retrieving album tracks for reordering", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public int getCount(UUID albumId) {
        return DatabaseUtils.getPerformanceData(albumId, connection, GET_COUNT);
    }

    @Override
    public void updatePosition(UUID id, int position) {
        DatabaseUtils.updateTrackPosition(id, position, connection, UPDATE_POSITION);
    }

    @Override
    public void removeTrack(UUID albumId, UUID trackId) {
        DatabaseUtils.removeTrack(albumId, trackId, connection, REMOVE_TRACK);
    }
}
