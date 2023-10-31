package com.vention.fm.repository.playlist_tracks;

import com.vention.fm.domain.model.playlist.PlaylistTracks;

import java.util.List;
import java.util.UUID;

public interface PlaylistTracksRepository {
    String GET_QUERY = "select pt.id as playlist_track_id, pt.track_position, " +
            "t.id as track_id, t.name as track_name, t.url as track_url, t.duration, t.playcount as track_playcount, t.listeners as track_listeners, t.is_blocked as track_is_blocked, " +
            "a.id as artist_id, a.name as artist_name, a.url as artist_url, a.playcount as artist_playcount, a.listeners as artist_listeners, a.is_blocked as artist_is_blocked " +
            "from playlist_tracks pt inner join tracks t on pt.track_id = t.id " +
            "inner join artists a on t.artist_id = a.id ";

    String INSERT = "insert into playlist_tracks(id, created_date, updated_date, is_blocked, playlist_id, track_id, track_position) values(?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = GET_QUERY + "where pt.id = ?";
    String GET_PLAYLIST_TRACKS = GET_QUERY + "where pt.playlist_id = ? order by pt.track_position asc";
    String GET_TRACKS_FOR_REORDER = GET_QUERY + "where pt.track_position > (select track_position from playlist_tracks where playlist_id = ? and track_id = ?) and pt.playlist_id = ? order by pt.track_position asc";
    String COUNT_PLAYLIST_TRACKS = "select count(*) from playlist_tracks where playlist_id = ?";
    String UPDATE_POSITION = "update playlist_tracks set updated_date = ?, track_position = ? where id = ?";
    String REMOVE_TRACK = "delete from playlist_tracks where playlist_id = ? and track_id = ?";

    void save(PlaylistTracks playlist);

    PlaylistTracks getById(UUID playlistTrackId);

    List<PlaylistTracks> getPlaylistTracks(UUID playlistId);

    List<PlaylistTracks> getPlaylistTracksToReorder(UUID playlistId, UUID trackId);

    int countPlaylistTracks(UUID playlistId);

    void updatePosition(UUID id, int trackPosition);

    void removeTrack(UUID playlistId, UUID trackId);
}
