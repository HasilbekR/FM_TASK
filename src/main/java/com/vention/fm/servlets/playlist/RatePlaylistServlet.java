package com.vention.fm.servlets.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.playlist.PlaylistRatingDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.PlaylistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/playlist/rate")
public class RatePlaylistServlet extends HttpServlet {
    private final PlaylistService playlistService = new PlaylistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            PlaylistRatingDto playlistRatingDto = objectMapper.readValue(req.getReader(), PlaylistRatingDto.class);
            String result = playlistService.ratePlaylist(playlistRatingDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
