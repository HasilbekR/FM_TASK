package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.album.AlbumRequestDto;
import com.vention.fm.domain.dto.album.AlbumResponseDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.AlbumTracksService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = {"/album/add-track", "/album/get-album", "/album/remove-track"})
public class AlbumTracksServlet extends HttpServlet {
    private final AlbumTracksService albumTracksService = new AlbumTracksService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            AlbumRequestDto albumTrackDto = objectMapper.readValue(req.getReader(), AlbumRequestDto.class);
            String result = albumTracksService.save(albumTrackDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String albumName = req.getParameter("name");
            String ownerId = req.getParameter("userId");
            AlbumResponseDto album = albumTracksService.getAlbum(albumName, UUID.fromString(ownerId));
            resp.getWriter().print(objectMapper.writeValueAsString(album));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            AlbumRequestDto albumTrack = objectMapper.readValue(req.getReader(), AlbumRequestDto.class);
            String result = albumTracksService.removeTrack(albumTrack);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
