package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.album.AlbumDto;
import com.vention.fm.service.AlbumTracksService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/album/get-album")
public class GetAlbumServlet extends HttpServlet {
    private final AlbumTracksService albumTracksService = new AlbumTracksService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String albumName = req.getParameter("name");
        String ownerId = req.getParameter("userId");
        AlbumDto album = albumTracksService.getAlbum(albumName, UUID.fromString(ownerId));
        resp.getWriter().print(objectMapper.writeValueAsString(album));
    }
}
