package com.vention.fm.repository.track;

import com.vention.fm.domain.model.track.Track;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TrackRepositoryImpl implements TrackRepository{
    private final Connection connection= Utils.getConnection();
    @Override
    public List<Track> getTrackListByArtist(UUID artistId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ARTIST);
            preparedStatement.setObject(1, artistId);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Track> tracks = new ArrayList<>();
            while (resultSet.next()){
                tracks.add(ResultSetMapper.mapTrack(resultSet));
            }
            return tracks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Track getTrackByName(String name) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return ResultSetMapper.mapTrack(resultSet);
            }
            throw new DataNotFoundException("Track with name " + name + " not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Track> searchTracksByName(String name) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_BY_NAME);
            preparedStatement.setString(1, "%"+name+"%");
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Track> tracks = new ArrayList<>();
            while (resultSet.next()){
                tracks.add(ResultSetMapper.mapTrack(resultSet));
            }
            return tracks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public List<Track> getAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Track> tracks = new ArrayList<>();
            while (resultSet.next()){
                tracks.add(ResultSetMapper.mapTrack(resultSet));
            }
            return tracks;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Track getTrackByNameAndArtist(String name, UUID artistId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_TRACK);
            preparedStatement.setString(1, name);
            preparedStatement.setObject(2, artistId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return ResultSetMapper.mapTrack(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Track with name " + name + " not found");
    }
    @Override
    public void save(Track track) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setObject(1, track.getId());
            preparedStatement.setObject(2, track.getCreatedDate());
            preparedStatement.setObject(3, track.getUpdatedDate());
            preparedStatement.setBoolean(4, track.getIsBlocked());
            preparedStatement.setString(5, track.getName());
            preparedStatement.setString(6, track.getUrl());
            if(track.getDuration() != null) {
                preparedStatement.setInt(7, track.getDuration());
            }else {
                preparedStatement.setNull(7, Types.INTEGER);
            }
            preparedStatement.setInt(8, track.getPlaycount());
            preparedStatement.setInt(9, track.getListeners());
            preparedStatement.setObject(10, track.getArtist().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void update(Track track) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setObject(1, track.getUpdatedDate());
            preparedStatement.setBoolean(2, track.getIsBlocked());
            preparedStatement.setString(3, track.getName());
            preparedStatement.setString(4, track.getUrl());
            preparedStatement.setInt(5, track.getPlaycount());
            preparedStatement.setInt(6, track.getListeners());
            preparedStatement.setObject(7, track.getArtist().getId());
            preparedStatement.setObject(8, track.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UUID getArtistId(UUID trackId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ARTIST_ID);
            preparedStatement.setObject(1, trackId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return (UUID) resultSet.getObject(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Track not found");
    }

    @Override
    public Boolean isBlocked(UUID trackId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(IS_BLOCKED);
            preparedStatement.setObject(1, trackId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Track not found");
    }

    @Override
    public void blockTrack(Boolean isBlocked, UUID trackId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(BLOCK_TRACK);
            preparedStatement.setBoolean(1, isBlocked);
            preparedStatement.setObject(2, trackId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}