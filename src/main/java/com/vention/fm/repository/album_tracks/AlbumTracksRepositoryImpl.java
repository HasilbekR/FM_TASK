package com.vention.fm.repository.album_tracks;

import com.vention.fm.domain.model.album.AlbumTracks;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.utils.DatabaseUtils;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class AlbumTracksRepositoryImpl implements AlbumTracksRepository {
    private final Connection connection = Utils.getConnection();

    @Override
    public void save(AlbumTracks albumTracks) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, albumTracks);
            preparedStatement.setObject(5, albumTracks.getAlbumId());
            preparedStatement.setObject(6, albumTracks.getTrackId());
            preparedStatement.setInt(7, albumTracks.getTrackPosition());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getCount(UUID albumId) {
        return DatabaseUtils.getData(albumId, connection, GET_COUNT);
    }
}
