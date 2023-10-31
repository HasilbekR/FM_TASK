package com.vention.fm.repository.album;

import com.vention.fm.domain.model.album.Album;

import java.util.List;
import java.util.UUID;

public interface AlbumRepository {
    String GET_QUERY = "select al.id as album_id, al.name as album_name, " +
            "a.id as artist_id, a.name as artist_name, a.url as artist_url, a.playcount as artist_playcount, a.listeners as artist_listeners, a.is_blocked as artist_is_blocked, " +
            "u.id as user_id, u.username, u.email, u.role, u.password, u.is_blocked as user_is_blocked " +
            "from albums al inner join artists a on al.artist_id = a.id " +
            "inner join users u on al.owner_id = u.id ";

    String INSERT = "insert into albums (id, created_date, updated_date, is_blocked, name, artist_id, owner_id) values(?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = GET_QUERY + "where al.id = ?";
    String GET_ALBUM_STATE = "select id, artist_id from albums where name = ? and owner_id = ?";
    String GET_ALBUM_WITH_OWNER_ID = GET_QUERY + "where al.name = ? and al.owner_id = ?";
    String GET_ALL = GET_QUERY + "where al.owner_id = ?";
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
