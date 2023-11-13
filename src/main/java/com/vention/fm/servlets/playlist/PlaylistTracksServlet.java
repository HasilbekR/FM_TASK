package com.vention.fm.servlets.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.playlist.PlaylistRequestDto;
import com.vention.fm.domain.dto.playlist.PlaylistResponseDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.PlaylistTracksService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = {"/playlist/add-track", "/playlist/get-playlist", "/playlist/remove-track"})
public class PlaylistTracksServlet extends HttpServlet {
    private final PlaylistTracksService playlistTracksService = new PlaylistTracksService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            PlaylistRequestDto playlistTrack = objectMapper.readValue(req.getReader(), PlaylistRequestDto.class);
            String result = playlistTracksService.addPlaylist(playlistTrack);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String playlistName = req.getParameter("name");
            String userId = req.getParameter("userId");
            PlaylistResponseDto playlist = playlistTracksService.getPlaylist(playlistName, UUID.fromString(userId));
            resp.getWriter().print(objectMapper.writeValueAsString(playlist));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            PlaylistRequestDto playlistTrack = objectMapper.readValue(req.getReader(), PlaylistRequestDto.class);
            String result = playlistTracksService.removeTrack(playlistTrack);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.getMethod();
        String requestURI = req.getRequestURI();
        if (method.equals("GET") && requestURI.equals("/playlist/get-playlist")) {
            doGet(req, resp);
        } else if (method.equals("POST") && requestURI.equals("/playlist/add-track")) {
            doPost(req, resp);
        } else if (method.equals("DELETE") && requestURI.equals("/playlist/remove-track")) {
            doDelete(req, resp);
        } else {
            Utils.methodNotAllowed(req, resp);
        }
    }
}
