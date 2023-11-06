package com.vention.fm.servlets.playlist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.playlist.PlaylistRequestDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.PlaylistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = {"/playlist/save", "/playlist/get-my-playlists", "/playlist/update", "/playlist/delete"})
public class CRUDPlaylistServlet extends HttpServlet {
    private final PlaylistService playlistService = new PlaylistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            PlaylistRequestDto playlistDto = objectMapper.readValue(req.getReader(), PlaylistRequestDto.class);
            String result = playlistService.save(playlistDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String ownerId = req.getParameter("userId");
            String json = objectMapper.writeValueAsString(playlistService.getAllByOwnerId(UUID.fromString(ownerId)));
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.getRequestURI();
        if ("/playlist/update".equals(method)) {
            try {
                PlaylistRequestDto playlistResponseDto = objectMapper.readValue(req.getReader(), PlaylistRequestDto.class);
                String result = playlistService.update(playlistResponseDto);
                resp.getWriter().print(result);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        } else {
            Utils.methodNotAllowed(req, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.getRequestURI();
        if ("/playlist/delete".equals(method)) {
            try {
                String playlistName = req.getParameter("name");
                UUID ownerId = UUID.fromString(req.getParameter("userId"));
                String result = playlistService.delete(playlistName, ownerId);
                resp.getWriter().print(result);
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        } else {
            Utils.methodNotAllowed(req, resp);
        }
    }

    //I need service method to prevent from methods that are not existed in this servlet
    //and I have to check if uri is correct for this doXXX method,
    //otherwise any uri with doXXX method can send request to this method
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.getMethod();
        String requestURI = req.getRequestURI();
        if (method.equals("GET") && requestURI.equals("/playlist/get-my-playlists")) {
            doGet(req, resp);
        } else if (method.equals("POST") && requestURI.equals("/playlist/save")) {
            doPost(req, resp);
        } else if (method.equals("PUT") && requestURI.equals("/playlist/update")) {
            doPut(req, resp);
        } else if (method.equals("DELETE") && requestURI.equals("/playlist/delete")) {
            doDelete(req, resp);
        } else {
            Utils.methodNotAllowed(req, resp);
        }
    }
}
