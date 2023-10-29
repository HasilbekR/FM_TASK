package com.vention.fm.repository.track;

import com.vention.fm.domain.model.track.Track;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.utils.DatabaseUtils;
import com.vention.fm.utils.Utils;
import com.vention.fm.mapper.ResultSetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackRepositoryImpl implements TrackRepository {
    private final Connection connection = Utils.getConnection();

    private static final Logger log = LoggerFactory.getLogger(TrackRepositoryImpl.class);

    @Override
    public void save(Track track) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, track);
            preparedStatement.setString(5, track.getName());
            preparedStatement.setString(6, track.getUrl());
            if (track.getDuration() != null) {
                preparedStatement.setInt(7, track.getDuration());
            } else {
                preparedStatement.setNull(7, Types.INTEGER);
            }
            preparedStatement.setInt(8, track.getPlaycount());
            preparedStatement.setInt(9, track.getListeners());
            preparedStatement.setObject(10, track.getArtist().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while saving track", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Track getById(UUID trackId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
            preparedStatement.setObject(1, trackId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapTrack(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving track", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Track getTrackByName(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapTrack(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving track", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Track getTrackState(String trackName) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TRACK_STATE);
            preparedStatement.setString(1, trackName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapTrackState(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving track state", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Track getTrackByNameAndArtist(String name, UUID artistId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TRACK);
            preparedStatement.setString(1, name);
            preparedStatement.setObject(2, artistId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapTrack(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving track", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<Track> getTrackListByArtist(UUID artistId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ARTIST);
            preparedStatement.setObject(1, artistId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Track> tracks = new ArrayList<>();
            while (resultSet.next()) {
                tracks.add(ResultSetMapper.mapTrack(resultSet));
            }
            return tracks;
        } catch (SQLException e) {
            log.error("Error occurred while retrieving tracks", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<Track> getAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Track> tracks = new ArrayList<>();
            while (resultSet.next()) {
                tracks.add(ResultSetMapper.mapTrack(resultSet));
            }
            return tracks;
        } catch (SQLException e) {
            log.error("Error occurred while retrieving tracks", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<Track> searchTracksByName(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_BY_NAME);
            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Track> tracks = new ArrayList<>();
            while (resultSet.next()) {
                tracks.add(ResultSetMapper.mapTrack(resultSet));
            }
            return tracks;
        } catch (SQLException e) {
            log.error("Error occurred while retrieving tracks", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void update(Track track) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setObject(1, track.getUpdatedDate());
            preparedStatement.setString(2, track.getName());
            preparedStatement.setString(3, track.getUrl());
            preparedStatement.setInt(4, track.getPlaycount());
            preparedStatement.setInt(5, track.getListeners());
            preparedStatement.setObject(6, track.getArtist().getId());
            preparedStatement.setObject(7, track.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while updating track", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void blockTrack(Boolean isBlocked, String trackName) {
        DatabaseUtils.block(isBlocked, trackName, connection, BLOCK_TRACK);
    }

}
