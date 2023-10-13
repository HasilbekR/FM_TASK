package com.vention.fm.repository.artist;

import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.repository.user.UserRepositoryImpl;
import com.vention.fm.utils.DatabaseUtils;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArtistRepositoryImpl implements ArtistRepository {
    private final Connection connection = Utils.getConnection();

    @Override
    public Artist getArtistByName(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement(GET_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapArtist(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Artist with name " + name + " not found");
    }

    @Override
    public UUID getIdByName(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ID_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getObject(1, UUID.class);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Artist with name " + name + " not found");
    }

    @Override
    public List<Artist> getAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Artist> artists = new ArrayList<>();
            while (resultSet.next()) {
                artists.add(ResultSetMapper.mapArtist(resultSet));
            }
            return artists;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Artist artist) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, artist);
            preparedStatement.setString(5, artist.getName());
            preparedStatement.setString(6, artist.getUrl());
            if (artist.getPlaycount() == null) {
                preparedStatement.setInt(7, 0);
            } else {
                preparedStatement.setInt(7, artist.getPlaycount());
            }
            if (artist.getListeners() == null) {
                preparedStatement.setInt(8, 0);
            } else {
                preparedStatement.setInt(8, artist.getListeners());
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Artist artist) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setObject(1, artist.getUpdatedDate());
            if (artist.getPlaycount() != null && artist.getPlaycount() != 0) {
                preparedStatement.setInt(2, artist.getPlaycount());
            } else {
                preparedStatement.setInt(2, getPlayCount(artist.getId()));
            }
            if (artist.getListeners() != null && artist.getListeners() != 0) {
                preparedStatement.setInt(3, artist.getListeners());
            } else {
                preparedStatement.setInt(3, getListeners(artist.getId()));
            }
            preparedStatement.setObject(4, artist.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Artist getArtistById(UUID id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapArtist(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Artist not found");
    }

    @Override
    public void blockArtist(Boolean isBlocked, UUID artistId) {
        DatabaseUtils.block(isBlocked, artistId, connection, BLOCK_ARTIST);
    }

    @Override
    public int getPlayCount(UUID artistId) {
        return DatabaseUtils.getData(artistId, connection, GET_PLAY_COUNT);
    }

    @Override
    public int getListeners(UUID artistId) {
        return DatabaseUtils.getData(artistId, connection, GET_LISTENERS);
    }

    @Override
    public Boolean isBlocked(UUID artistId) {
        return DatabaseUtils.isBlocked(artistId, connection, IS_BLOCKED);
    }
}
