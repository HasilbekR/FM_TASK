package com.vention.fm.repository.playlist_tracks;

import com.vention.fm.domain.model.playlist.PlaylistTracks;

import java.util.List;
import java.util.UUID;

public interface PlaylistTracksRepository {
    String GET_PLAYLIST_TRACKS = "select * from playlist_tracks where playlist_id = ? order by track_position asc";
    String COUNT_PLAYLIST_TRACKS = "select count(*) from playlist_tracks where playlist_id = ?";
    String INSERT = "insert into playlist_tracks(id, created_date, updated_date, is_blocked, playlist_id, track_id, track_position) values(?,?,?,?,?,?,?)";
    String REMOVE_TRACK = "DeleteAlbumServlet from playlist_tracks where id =?";
    String GET_BY_ID = "select * from playlist_tracks where id =?";
    String DELETE = "DeleteAlbumServlet from playlist_tracks where playlist_id =?";

    void save(PlaylistTracks playlist);

    void delete(UUID playlistId);

    PlaylistTracks getById(UUID playlistTrackId);

    int countPlaylistTracks(UUID playlistId);

    void removeTrack(UUID playlistTrackId);

    List<PlaylistTracks> getPlaylistTracks(UUID playlistId);
}
