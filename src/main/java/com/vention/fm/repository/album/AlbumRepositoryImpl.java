package com.vention.fm.repository.album;

import com.vention.fm.domain.model.album.Album;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.utils.DatabaseUtils;
import com.vention.fm.utils.Utils;
import com.vention.fm.mapper.ResultSetMapper;
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

public class AlbumRepositoryImpl implements AlbumRepository {
    private final Connection connection = Utils.getConnection();
    private static final Logger log = LoggerFactory.getLogger(AlbumRepositoryImpl.class);

    @Override
    public void save(Album album) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            DatabaseUtils.setValues(preparedStatement, album);
            preparedStatement.setString(5, album.getName());
            preparedStatement.setObject(6, album.getArtist().getId());
            preparedStatement.setObject(7, album.getOwner().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while saving album", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Album getById(UUID id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_ID);
            preparedStatement.setObject(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapAlbum(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving album", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Album getAlbumState(String albumName, UUID ownerId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALBUM_STATE);
            preparedStatement.setString(1, albumName);
            preparedStatement.setObject(2, ownerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapAlbumState(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving album state", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public Album getAlbum(String albumName, UUID ownerId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALBUM_WITH_OWNER_ID);
            preparedStatement.setString(1, albumName);
            preparedStatement.setObject(2, ownerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return ResultSetMapper.mapAlbum(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            log.error("Error occurred while retrieving album", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public List<Album> getAll(UUID ownerId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            preparedStatement.setObject(1, ownerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Album> albums = new ArrayList<>();
            while (resultSet.next()) {
                albums.add(ResultSetMapper.mapAlbum(resultSet));
            }
            return albums;
        } catch (SQLException e) {
            log.error("Error occurred while retrieving artist", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void update(Album album) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setObject(1, LocalDateTime.now());
            preparedStatement.setString(2, album.getName());
            preparedStatement.setObject(3, album.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while updating album", e);
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void delete(UUID albumId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setObject(1, albumId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Error occurred while deleting album", e);
            throw new BadRequestException(e.getMessage());
        }
    }
}
