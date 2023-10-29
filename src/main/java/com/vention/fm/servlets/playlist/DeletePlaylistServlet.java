package com.vention.fm.servlets.playlist;

import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.PlaylistService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/playlist/delete")
public class DeletePlaylistServlet extends HttpServlet {
    private final PlaylistService playlistService = new PlaylistService();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String playlistName = req.getParameter("name");
            UUID ownerId = UUID.fromString(req.getParameter("userId"));
            String result = playlistService.delete(playlistName, ownerId);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
