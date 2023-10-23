package com.vention.fm.repository.playlist;

import com.vention.fm.domain.model.playlist.Playlist;

import java.util.List;
import java.util.UUID;

public interface PlaylistRepository {
    String INSERT = "insert into playlists (id, created_date, updated_date, is_blocked, name, is_public, like_count, dislike_count, owner_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = "select * from playlists where id = ?";
    String GET_BY_NAME = "select * from playlists where name = ?";
    String GET_PLAYLIST_STATE = "select id, owner_id, is_public from playlists where name = ?";
    String GET_ALL_BY_OWNER_ID = "select * from playlists where owner_id = ?";
    String GET_AVAILABLE_PLAYLISTS = "select * from playlists where is_public = true";
    String UPDATE = "update playlists set updated_date = ?, name = ?, is_public = ? where owner_id = ? and id = ?;";
    String RATE = "update playlists set updated_date = ?, like_count = ?, dislike_count = ? where id = ?";
    String DELETE = "delete from playlists where id = ?";

    void save(Playlist playlist);

    Playlist getById(UUID id);

    Playlist getPlaylistByName(String name);

    Playlist getPlaylistState(String name);

    List<Playlist> getAllByOwnerId(UUID ownerId);

    List<Playlist> getAvailablePlaylists();

    void update(Playlist playlist);

    void rate(int likeCount, int dislikeCount, UUID playlistId);

    void delete(UUID id);
}
