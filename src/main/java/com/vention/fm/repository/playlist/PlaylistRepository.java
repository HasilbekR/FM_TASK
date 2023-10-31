package com.vention.fm.repository.playlist;

import com.vention.fm.domain.model.playlist.Playlist;

import java.util.List;
import java.util.UUID;

public interface PlaylistRepository {
    String GET_QUERY = "select p.id as playlist_id, p.name as playlist_name, p.is_public, p.like_count, p.dislike_count, " +
            "u.id as user_id, u.username, u.email, u.role, u.password, u.is_blocked as user_is_blocked " +
            "from playlists p inner join users u on p.owner_id = u.id ";

    String INSERT = "insert into playlists (id, created_date, updated_date, is_blocked, name, is_public, like_count, dislike_count, owner_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String GET_BY_ID = GET_QUERY + "where p.id = ?";
    String GET_BY_NAME = GET_QUERY + "where p.name = ?";
    String GET_PLAYLIST_STATE = "select id, owner_id, is_public from playlists where name = ?";
    String GET_ALL_BY_OWNER_ID = GET_QUERY + "where p.owner_id = ?";
    String GET_AVAILABLE_PLAYLISTS = GET_QUERY + "where p.is_public = true";
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
