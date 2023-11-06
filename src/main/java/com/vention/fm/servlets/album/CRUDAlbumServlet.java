package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.album.AlbumRequestDto;
import com.vention.fm.domain.dto.album.AlbumResponseDto;
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

@WebServlet(urlPatterns = {"/album/save", "/album/get-all", "/album/update", "/album/delete"})
public class CRUDAlbumServlet extends HttpServlet {
    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    private final AlbumService albumService = new AlbumService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            AlbumRequestDto albumRequestDto = objectMapper.readValue(req.getReader(), AlbumRequestDto.class);
            String result = albumService.save(albumRequestDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String ownerId = req.getParameter("userId");
            List<AlbumResponseDto> albums = albumService.getAll(UUID.fromString(ownerId));
            resp.getWriter().write(objectMapper.writeValueAsString(albums));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            AlbumRequestDto albumDto = objectMapper.readValue(req.getReader(), AlbumRequestDto.class);
            String result = albumService.update(albumDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String albumName = req.getParameter("name");
            String ownerId = req.getParameter("userId");
            String result = albumService.delete(albumName, ownerId);
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
        if (method.equals("GET") && requestURI.equals("/album/get-all")) {
            doGet(req, resp);
        } else if (method.equals("POST") && requestURI.equals("/album/save")) {
            doPost(req, resp);
        } else if (method.equals("PUT") && requestURI.equals("/album/update")) {
            doPut(req, resp);
        } else if (method.equals("DELETE") && requestURI.equals("/album/delete")) {
            doDelete(req, resp);
        } else {
            Utils.methodNotAllowed(req, resp);
        }
    }
}
