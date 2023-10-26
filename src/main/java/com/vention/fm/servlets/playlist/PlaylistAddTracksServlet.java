package com.vention.fm.servlets.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.playlist.PlaylistAddTrackDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.PlaylistTracksService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/playlist/add-track")
public class PlaylistAddTracksServlet extends HttpServlet {
    private final PlaylistTracksService playlistTracksService = new PlaylistTracksService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            PlaylistAddTrackDto playlistTrack = objectMapper.readValue(req.getReader(), PlaylistAddTrackDto.class);
            String result = playlistTracksService.addPlaylist(playlistTrack);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
