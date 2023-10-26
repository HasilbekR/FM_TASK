package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.album.AlbumAddTrackDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.AlbumTracksService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/album/add-track")
public class AddTracksToAlbumServlet extends HttpServlet {
    private final AlbumTracksService albumTracksService = new AlbumTracksService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            AlbumAddTrackDto albumAddTrackDto = objectMapper.readValue(req.getReader(), AlbumAddTrackDto.class);
            String result = albumTracksService.save(albumAddTrackDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
