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
import java.util.UUID;

@WebServlet(urlPatterns = {"/playlist/save", "/playlist/get-my-playlists", "/playlist/update", "/playlist/delete"})
public class CRUDPlaylistServlet extends HttpServlet {
    private final PlaylistService playlistService = new PlaylistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            PlaylistRequestDto playlistDto = objectMapper.readValue(req.getReader(), PlaylistRequestDto.class);
            String result = playlistService.save(playlistDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String ownerId = req.getParameter("userId");
            String json = objectMapper.writeValueAsString(playlistService.getAllByOwnerId(UUID.fromString(ownerId)));
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            PlaylistRequestDto playlistResponseDto = objectMapper.readValue(req.getReader(), PlaylistRequestDto.class);
            String result = playlistService.update(playlistResponseDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String playlistName = req.getParameter("name");
            UUID ownerId = UUID.fromString(req.getParameter("userId"));
            String result = playlistService.delete(playlistName, ownerId);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
