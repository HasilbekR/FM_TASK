package com.vention.fm.repository.album_tracks;

import com.vention.fm.domain.model.album.AlbumTracks;

import java.util.List;
import java.util.UUID;

public interface AlbumTracksRepository {
    String GET_QUERY = "select at.id as album_track_id, at.track_position, " +
            "t.id as track_id, t.name as track_name, t.url as track_url, t.duration, t.playcount as track_playcount, t.listeners as track_listeners, t.is_blocked as track_is_blocked, " +
            "a.id as artist_id, a.name as artist_name, a.url as artist_url, a.playcount as artist_playcount, a.listeners as artist_listeners, a.is_blocked as artist_is_blocked " +
            "from album_tracks at inner join tracks t on at.track_id = t.id " +
            "inner join artists a on t.artist_id = a.id ";

    String INSERT = "insert into album_tracks(id, created_date, updated_date, is_blocked, album_id, track_id, track_position) values(?, ?, ?, ?, ?, ?, ?)";
    String GET_ALBUM_TRACKS = GET_QUERY + "where at.album_id = ? order by track_position asc";
    String GET_TRACKS_FOR_REORDER = GET_QUERY + "where at.track_position > (select track_position from album_tracks where album_id = ? and track_id = ?) and at.album_id = ? order by at.track_position asc";
    String GET_COUNT = "select count(*) from album_tracks where album_id = ?";
    String UPDATE_POSITION = "update album_tracks set updated_date = ?, track_position = ? where id = ?";
    String REMOVE_TRACK = "delete from album_tracks where album_id = ? and track_id = ?";

    void save(AlbumTracks albumTracks);

    List<AlbumTracks> getAlbumTracks(UUID albumId);

    List<AlbumTracks> getAlbumTracksToReorder(UUID albumId, UUID trackId);

    int getCount(UUID albumId);

    void updatePosition(UUID id, int position);

    void removeTrack(UUID albumId, UUID trackId);
}
