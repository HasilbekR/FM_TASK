package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.album.AlbumDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.AlbumService;
import com.vention.fm.utils.Utils;
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String ownerId = req.getParameter("userId");
            List<AlbumDto> albums = albumService.getAll(UUID.fromString(ownerId));
            resp.getWriter().write(objectMapper.writeValueAsString(albums));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
