package com.vention.fm.repository.album;

import com.vention.fm.domain.model.album.Album;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.utils.Utils;
import com.vention.fm.utils.ResultSetMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AlbumRepositoryImpl implements AlbumRepository{
    private final Connection connection = Utils.getConnection();
    @Override
    public Album getByName(String name, UUID ownerId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BY_NAME);
            preparedStatement.setString(1, name);
            preparedStatement.setObject(2, ownerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return ResultSetMapper.mapAlbum(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Album with name " + name + " not found");
    }

    @Override
    public List<Album> getAll(UUID ownerId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            preparedStatement.setObject(1, ownerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Album> albums = new ArrayList<>();
            while(resultSet.next()){
                albums.add(ResultSetMapper.mapAlbum(resultSet));
            }
            return albums;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Album album) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT);
            preparedStatement.setObject(1, album.getId());
            preparedStatement.setObject(2, album.getCreatedDate());
            preparedStatement.setObject(3, album.getUpdatedDate());
            preparedStatement.setBoolean(4, album.getIsBlocked());
            preparedStatement.setString(5, album.getName());
            preparedStatement.setObject(6, album.getArtistId());
            preparedStatement.setObject(7, album.getOwnerId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UUID getArtistId(UUID id) {
        return getId(id, GET_ARTIST_ID);
    }

    @Override
    public UUID getOwnerId(UUID albumId) {
        return getId(albumId, GET_OWNER_ID);
    }

    @Override
    public void delete(UUID albumId) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);
            preparedStatement.setObject(1, albumId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UUID getId(UUID albumId, String query) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setObject(1, albumId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){return resultSet.getObject(1, UUID.class);}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        throw new DataNotFoundException("Album not found");
    }
}
