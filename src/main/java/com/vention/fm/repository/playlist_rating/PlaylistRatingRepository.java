package com.vention.fm.repository.playlist_rating;

import com.vention.fm.domain.model.playlist.PlaylistRating;

import java.util.UUID;

public interface PlaylistRatingRepository {
    String INSERT = "insert into playlist_rating(id, created_date, updated_date, is_blocked, playlist_id, user_id, is_liked) values(?, ?, ?, ?, ?, ?, ?)";
    String GET = "select id as playlist_rating_id, is_liked from playlist_rating where playlist_id = ? and user_id = ?";
    String GET_LIKE_COUNT = "select count(*) from playlist_rating where playlist_id = ? and is_liked = true";
    String GET_DISLIKE_COUNT = "select count(*) from playlist_rating where playlist_id = ? and is_liked = false";
    String UPDATE = "update playlist_rating set updated_date = ?, is_liked = ? where playlist_id = ? and user_id = ?";
    String DELETE = "delete from playlist_rating where playlist_id = ? and user_id = ?";

    void save(PlaylistRating playlistRating);

    PlaylistRating get(UUID playlistId, UUID userId);

    int getLikeCount(UUID playlistId);

    int getDislikeCount(UUID playlistId);

    void update(UUID playlistId, UUID userId, Boolean isLiked);

    void delete(UUID playlistId, UUID userId);
}
