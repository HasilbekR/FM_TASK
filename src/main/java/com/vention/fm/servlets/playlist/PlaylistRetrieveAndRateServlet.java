package com.vention.fm.servlets.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.playlist.PlaylistRequestDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.PlaylistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/playlist/get-available-playlists", "/playlist/rate"})
public class PlaylistRetrieveAndRateServlet extends HttpServlet {
    private final PlaylistService playlistService = new PlaylistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = objectMapper.writeValueAsString(playlistService.getAvailablePlaylists());
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            PlaylistRequestDto playlistRatingDto = objectMapper.readValue(req.getReader(), PlaylistRequestDto.class);
            String result = playlistService.ratePlaylist(playlistRatingDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.getMethod();
        String requestURI = req.getRequestURI();
        if (method.equals("GET") && requestURI.equals("/playlist/get-available-playlists")) {
            doGet(req, resp);
        } else if (method.equals("PUT") && requestURI.equals("/playlist/rate")) {
            doPut(req, resp);
        } else {
            Utils.methodNotAllowed(req, resp);
        }
    }
}
