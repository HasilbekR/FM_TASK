package com.vention.fm.repository.track;

import com.vention.fm.domain.model.track.Track;

import java.util.List;
import java.util.UUID;

public interface TrackRepository {
    String SEARCH_BY_NAME = "select * from tracks where name ilike ?";
    String GET_BY_NAME = "select * from tracks where name =?";
    String GET_TRACK = "select * from tracks where name =? and artist_id =?";
    String GET_BY_ARTIST = "select * from tracks where artist_id =?";
    String GET_ALL = "select * from tracks";
    String INSERT = "insert into tracks(id, created_date, updated_date, is_blocked, name, url, duration, playCount, listeners, artist_id) values(?,?,?,?,?,?,?,?,?,?)";
    String UPDATE = "update tracks set updated_date =?, is_blocked =?, name =?, url =?, playCount =?, listeners =?, artist_id =? where id =?";
    String GET_ARTIST_ID = "select artist_id from tracks where id = ?";
    String IS_BLOCKED = "select is_blocked from tracks where id =?";
    String BLOCK_TRACK = "update tracks set is_blocked = ? where id = ?";
    List<Track> searchTracksByName(String name);
    Track getTrackByName(String name);
    List<Track> getTrackListByArtist(UUID artistId);
    List<Track> getAll();
    Track getTrackByNameAndArtist(String name, UUID artistId);
    void save(Track track);
    void update(Track track);
    UUID getArtistId(UUID trackId);

    Boolean isBlocked(UUID trackId);

    void blockTrack(Boolean isBlocked, UUID trackId);

}
