package com.vention.fm.servlets.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.playlist.PlaylistCreateDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.PlaylistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/playlist/save")
public class CreatePlaylistServlet extends HttpServlet {
    private final PlaylistService playlistService = new PlaylistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            PlaylistCreateDto playlistDto = objectMapper.readValue(req.getReader(), PlaylistCreateDto.class);
            String result = playlistService.save(playlistDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
