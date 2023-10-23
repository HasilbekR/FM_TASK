package com.vention.fm.servlets.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.playlist.PlaylistUpdateDto;
import com.vention.fm.service.PlaylistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/playlist/update")
public class UpdatePlaylistServlet extends HttpServlet {
    private final PlaylistService playlistService = new PlaylistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PlaylistUpdateDto playlistDto = objectMapper.readValue(req.getReader(), PlaylistUpdateDto.class);
        playlistService.update(playlistDto);
    }
}
