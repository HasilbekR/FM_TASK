package com.vention.fm.repository.playlist_tracks;

import com.vention.fm.domain.model.playlist.PlaylistTracks;

import java.util.List;
import java.util.UUID;

public interface PlaylistTracksRepository {
    String INSERT = "insert into playlist_tracks(id, created_date, updated_date, is_blocked, playlist_id, track_id, track_position) values(?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = "select * from playlist_tracks where id = ?";
    String GET_PLAYLIST_TRACKS = "select * from playlist_tracks where playlist_id = ? order by track_position asc";
    String GET_TRACKS_FOR_REORDER = "select * from playlist_tracks where track_position > (select track_position from playlist_tracks where playlist_id = ? and track_id = ?) and playlist_id = ? order by track_position asc";
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
