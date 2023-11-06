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

    //I need service method to prevent from methods that are not existed in this servlet
    //and I have to check if uri is correct for this doXXX method,
    //otherwise any uri with doXXX method can send request to this method
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.getMethod();
        String requestURI = req.getRequestURI();
        if (method.equals("GET") && requestURI.equals("/album/get-album")) {
            doGet(req, resp);
        } else if (method.equals("POST") && requestURI.equals("/album/add-track")) {
            doPost(req, resp);
        } else if (method.equals("DELETE") && requestURI.equals("/album/remove-track")) {
            doDelete(req, resp);
        } else {
            Utils.methodNotAllowed(req, resp);
        }
    }
}
