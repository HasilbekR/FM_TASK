package com.vention.fm.repository.album;

import com.vention.fm.domain.model.album.Album;

import java.util.List;
import java.util.UUID;

public interface AlbumRepository {
    String GET_BY_NAME = "select * from albums where name =? and owner_id = ?";
    String GET_ALL = "select * from albums where owner_id = ?";
    String INSERT = "insert into albums (id, created_date, updated_date, is_blocked, name, artist_id, owner_id) values(?,?,?,?,?,?,?)";
    String GET_ARTIST_ID = "select artist_id from albums where id = ?";
    String GET_OWNER_ID = "select owner_id from albums where id = ?";
    String DELETE = "delete from albums where id = ?";

    Album getByName(String name, UUID ownerId);

    List<Album> getAll(UUID ownerId);

    void save(Album album);

    UUID getArtistId(UUID albumId);

    UUID getOwnerId(UUID albumId);

    void delete(UUID albumId);
}
