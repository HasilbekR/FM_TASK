package com.vention.fm.repository.artist;

import com.vention.fm.domain.model.artist.Artist;

import java.util.List;
import java.util.UUID;

public interface ArtistRepository {
    String GET_ID_BY_NAME = "select id from artists where name =?";
    String GET_BY_NAME = "select * from artists where name = ?";
    String GET_BY_ID = "select * from artists where id = ?";
    String GET_ALL = "select * from artists";
    String INSERT = "insert into artists(id, created_date, updated_date, is_blocked, name, url, playcount, listeners) values(?,?,?,?,?,?,?,?)";
    String UPDATE = "UPDATE artists SET updated_date=?, playcount=?, listeners=? WHERE id=?";
    String GET_PLAY_COUNT = "select playcount from artists where id =? ";
    String GET_LISTENERS = "select listeners from artists where id = ?";
    String BLOCK_ARTIST = "update artists set is_blocked = ? where id = ?";
    String IS_BLOCKED = "select is_blocked from artists where id = ?";

    Artist getArtistByName(String name);

    UUID getIdByName(String name);

    List<Artist> getAll();

    void save(Artist artist);

    void update(Artist artist);

    Artist getArtistById(UUID id);

    void blockArtist(Boolean isBlocked, UUID artistId);

    int getPlayCount(UUID artistId);

    int getListeners(UUID artistId);

    Boolean isBlocked(UUID artistId);
}
