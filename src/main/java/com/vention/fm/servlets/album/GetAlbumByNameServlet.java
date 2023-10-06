package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.model.album.Album;
import com.vention.fm.service.AlbumService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/album/get-by-name")
public class GetAlbumByNameServlet extends HttpServlet {

    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    private final AlbumService albumService = new AlbumService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String ownerId = req.getParameter("ownerId");
        Album album = albumService.getAlbumByName(name, UUID.fromString(ownerId));
        resp.getWriter().write(objectMapper.writeValueAsString(album));
    }
}