package com.vention.fm.repository.track;

import com.vention.fm.domain.model.track.Track;

import java.util.List;
import java.util.UUID;

public interface TrackRepository {
    String GET_QUERY = """
            select t.id as track_id, t.name as track_name, t.url as track_url, t.duration, t.playcount as track_playcount, t.listeners as track_listeners, t.is_blocked as track_is_blocked,
            a.id as artist_id, a.name as artist_name, a.url as artist_url, a.playcount as artist_playcount, a.listeners as artist_listeners, a.is_blocked as artist_is_blocked
            from tracks t inner join artists a on t.artist_id = a.id""";

    String INSERT = "insert into tracks(id, created_date, updated_date, is_blocked, name, url, duration, playCount, listeners, artist_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = GET_QUERY + "where t.id = ?";
    String GET_BY_NAME = GET_QUERY + "where t.name = ?";
    String GET_TRACK_STATE = "select id, is_blocked, artist_id from tracks where name = ?";
    String GET_TRACK_WITH_ARTIST = GET_QUERY + "where t.name = ? and t.artist_id = ?";
    String GET_BY_ARTIST = GET_QUERY + "where t.artist_id = ?";
    //I used ilike to perform case-insensitive searches with pattern matching
    String SEARCH_BY_NAME = GET_QUERY + "t.name ilike ?";
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
