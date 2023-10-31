package com.vention.fm.repository.artist;

import com.vention.fm.domain.model.artist.Artist;

import java.util.List;
import java.util.UUID;

public interface ArtistRepository {
    String GET_QUERY = "select id as artist_id, name as artist_name, url as artist_url, playcount as artist_playcount, listeners as artist_listeners, is_blocked as artist_is_blocked from artists ";

    String INSERT = "insert into artists(id, created_date, updated_date, is_blocked, name, url, playcount, listeners) values(?, ?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = GET_QUERY + "where id = ?";
    String GET_BY_NAME = GET_QUERY + "where name = ?";
    String GET_ARTIST_STATE = "select id, is_blocked from artists where name = ?";
    String GET_ID_BY_NAME = "select id from artists where name = ?";
    String GET_PLAY_COUNT = "select playcount from artists where id = ? ";
    String GET_LISTENERS = "select listeners from artists where id = ?";
    String IS_BLOCKED = "select is_blocked from artists where id = ?";
    String UPDATE = "update artists set updated_date = ?, playcount = ?, listeners = ? where id = ?";
    String BLOCK_ARTIST = "update artists set is_blocked = ? where name = ?";

    void save(Artist artist);

    Artist getArtistByName(String name);

    Artist getArtistState(String name);

    List<Artist> getAll();

    UUID getIdByName(String name);

    int getPlayCount(UUID artistId);

    int getListeners(UUID artistId);

    boolean isBlocked(UUID artistId);

    void update(Artist artist);

    void blockArtist(Boolean isBlocked, String artistName);
}
