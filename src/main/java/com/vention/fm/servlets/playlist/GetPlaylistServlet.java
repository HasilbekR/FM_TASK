package com.vention.fm.servlets.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.playlist.PlaylistDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.PlaylistTracksService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/playlist/get-playlist")
public class GetPlaylistServlet extends HttpServlet {
    private final PlaylistTracksService playlistTracksService = new PlaylistTracksService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String playlistName = req.getParameter("name");
            String userId = req.getParameter("userId");
            PlaylistDto playlist = playlistTracksService.getPlaylist(playlistName, UUID.fromString(userId));
            resp.getWriter().print(objectMapper.writeValueAsString(playlist));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
