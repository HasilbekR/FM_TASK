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
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/album/get-all")
public class GetAllAlbumsServlet extends HttpServlet {
    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    private final AlbumService albumService = new AlbumService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ownerId = req.getParameter("ownerId");
        List<Album> albums = albumService.getAll(UUID.fromString(ownerId));
        resp.getWriter().write(objectMapper.writeValueAsString(albums));
    }
}
