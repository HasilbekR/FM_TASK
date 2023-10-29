package com.vention.fm.repository.track;

import com.vention.fm.domain.model.track.Track;

import java.util.List;
import java.util.UUID;

public interface TrackRepository {
    String INSERT = "insert into tracks(id, created_date, updated_date, is_blocked, name, url, duration, playCount, listeners, artist_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = "select * from tracks where id = ?";
    String GET_BY_NAME = "select * from tracks where name = ?";
    String GET_TRACK_STATE = "select id, is_blocked, artist_id from tracks where name = ?";
    String GET_TRACK = "select * from tracks where name = ? and artist_id = ?";
    String GET_BY_ARTIST = "select * from tracks where artist_id = ?";
    String GET_ALL = "select * from tracks";
    //I used ilike to perform case-insensitive searches with pattern matching
    String SEARCH_BY_NAME = "select * from tracks where name ilike ?";
    String UPDATE = "update tracks set updated_date = ?, name = ?, url = ?, playCount = ?, listeners = ?, artist_id = ? where id = ?";
    String BLOCK_TRACK = "update tracks set is_blocked = ? where name = ?";

    void save(Track track);

    Track getById(UUID trackId);

    Track getTrackByName(String name);

    Track getTrackState(String trackName);

    Track getTrackByNameAndArtist(String name, UUID artistId);

    List<Track> getTrackListByArtist(UUID artistId);

    List<Track> getAll();

    List<Track> searchTracksByName(String name);

    void update(Track track);

    void blockTrack(Boolean isBlocked, String trackName);
}
