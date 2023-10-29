package com.vention.fm.servlets.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.PlaylistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/playlist/get-available-playlists")
public class GetAvailablePlaylistsServlet extends HttpServlet {
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
}
