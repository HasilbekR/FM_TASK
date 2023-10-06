package com.vention.fm.repository.playlist;

import com.vention.fm.domain.model.playlist.Playlist;

import java.util.List;
import java.util.UUID;

public interface PlaylistRepository {
    String GET_BY_NAME = "select * from playlists where name =?";
    String GET_ALL_BY_OWNER_ID = "select * from playlists where owner_id =?";
    String GET_BY_ID = "select * from playlists where id =?";
    String GET_AVAILABLE_PLAYLISTS = "select * from playlists where is_public = true";
    String INSERT = "insert into playlists (id, created_date, updated_date, is_blocked, name, is_public, like_count, dislike_count, owner_id) values(?,?,?,?,?,?,?,?,?)";
    String DELETE = "DeleteAlbumServlet from playlists where id =?";
    String UPDATE = "UPDATE playlists SET updated_date=?, name=?, is_public =? WHERE owner_id=? and id=?;";
    String RATE = "UPDATE playlists SET updated_date=?, like_count = ?, dislike_count = ? where id=?";

    Playlist getPlaylistByName(String name);
    Playlist getPlaylistById(UUID id);
    List<Playlist> getAvailablePlaylists();
    void save(Playlist playlist);
    void delete(UUID id);
    void rate(int likeCount, int dislikeCount, UUID playlistId);

    List<Playlist> getAllByOwnerId(UUID ownerId);

    void update(Playlist playlist);
}
