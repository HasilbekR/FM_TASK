package com.vention.fm.service;

import com.vention.fm.domain.model.playlist.PlaylistRating;
import com.vention.fm.repository.playlist_rating.PlaylistRatingRepository;
import com.vention.fm.repository.playlist_rating.PlaylistRatingRepositoryImpl;

import java.util.UUID;

public class PlaylistRatingService {
    private final PlaylistRatingRepository playlistRatingRepository = new PlaylistRatingRepositoryImpl();

    /**
     * Saving method works when used has not rated the playlist yet
     *
     * @param playlistRating -
     */
    public void save(PlaylistRating playlistRating) {
        playlistRatingRepository.save(playlistRating);
    }

    /**
     * This method is used to change rating of a playlist
     * It works when user liked first and then clicks dislike button or opposite
     *
     * @param playlistId - the playlist that is rated
     * @param userId     - the user who is rating
     * @param isLiked    - whether the user liked playlist or not
     */
    public void update(UUID playlistId, UUID userId, Boolean isLiked) {
        playlistRatingRepository.update(playlistId, userId, isLiked);
    }

    /**
     * This method is used to remove rating, it works when the user sends the same rating again
     *
     * @param playlistId - the playlist that is rated
     * @param userId     - the user who is rating
     */
    public void delete(UUID playlistId, UUID userId) {
        playlistRatingRepository.delete(playlistId, userId);
    }

    public PlaylistRating get(UUID playlistId, UUID userId) {
        return playlistRatingRepository.get(playlistId, userId);
    }

    public Integer getLikeCount(UUID playlistId) {
        return playlistRatingRepository.getLikeCount(playlistId);
    }

    public Integer getDislikeCount(UUID playlistId) {
        return playlistRatingRepository.getDislikeCount(playlistId);
    }
}
