package com.vention.fm.servlets.playlist;

import com.vention.fm.domain.model.playlist.PlaylistRating;
import com.vention.fm.exception.DataNotFoundException;
import com.vention.fm.service.PlaylistRatingService;
import com.vention.fm.service.PlaylistService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

@WebServlet(urlPatterns = "/playlist/rate")
public class RatePlaylistServlet extends HttpServlet {
    private final PlaylistService playlistService = new PlaylistService();
    private final PlaylistRatingService playlistRatingService = new PlaylistRatingService();
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp){
        UUID playlistId = UUID.fromString(req.getParameter("playlistId"));
        UUID userId = UUID.fromString(req.getParameter("userId"));
        Boolean isLiked = Boolean.parseBoolean(req.getParameter("isLiked"));
        try {
            PlaylistRating rating = playlistRatingService.get(playlistId, userId);
            // if user changes rating "like->dislike", rating is updated
            if (!isLiked.equals(rating.getIsLiked())) {
                playlistRatingService.update(playlistId, userId, isLiked);
                playlistService.rate(playlistId);
            }
            // if user rates the same again(like->like), rating is deleted
            else {
                playlistRatingService.delete(playlistId, userId);
                playlistService.rate(playlistId);
            }
        }
        // if user has not rated playlist then new rating is created
        catch (DataNotFoundException e){
            PlaylistRating playlistRating = PlaylistRating.builder()
                    .playlistId(playlistId)
                    .userId(userId)
                    .isLiked(isLiked)
                    .build();
            playlistRatingService.save(playlistRating);
            playlistService.rate(playlistId);
        }
    }
}
