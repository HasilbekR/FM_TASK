package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.model.album.AlbumTracks;
import com.vention.fm.service.AlbumService;
import com.vention.fm.service.AlbumTracksService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/album/get-tracks")
public class GetAlbumTracksServlet extends HttpServlet {
    private final AlbumTracksService albumTracksService = new AlbumTracksService();
    private final AlbumService albumService = new AlbumService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String albumId = req.getParameter("albumId");
        String ownerId = req.getParameter("ownerId");
        UUID ownerId1 = albumService.getOwnerId(UUID.fromString(albumId));
        if(!ownerId.equals(ownerId1.toString())) throw new AccessDeniedException("Access denied");

        List<AlbumTracks> albumTracks = albumTracksService.getAlbumTracks(UUID.fromString(albumId));
        resp.getWriter().print(objectMapper.writeValueAsString(albumTracks));
    }
}
