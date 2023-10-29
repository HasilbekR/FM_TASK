package com.vention.fm.repository.album;

import com.vention.fm.domain.model.album.Album;

import java.util.List;
import java.util.UUID;

public interface AlbumRepository {
    String INSERT = "insert into albums (id, created_date, updated_date, is_blocked, name, artist_id, owner_id) values(?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = "select * from albums where id = ?";
    String GET_ALBUM_STATE = "select id, artist_id from albums where name = ? and owner_id = ?";
    String GET_ALBUM = "select * from albums where name = ? and owner_id = ?";
    String GET_ALL = "select * from albums where owner_id = ?";
    String UPDATE = "update albums set updated_date = ?, name = ? where id = ? ";
    String DELETE = "delete from albums where id = ?";


    void save(Album album);

    Album getById(UUID id);

    Album getAlbumState(String albumName, UUID ownerId);

    Album getAlbum(String albumName, UUID ownerId);

    List<Album> getAll(UUID ownerId);

    void update(Album album);

    void delete(UUID albumId);
}
