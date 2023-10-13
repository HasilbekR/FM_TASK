package com.vention.fm.servlets.playlist;

import com.vention.fm.service.PlaylistService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/playlist/DeleteAlbumServlet")
public class DeletePlaylistServlet extends HttpServlet {
    private final PlaylistService playlistService = new PlaylistService();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID playlistId = UUID.fromString(req.getParameter("playlistId"));
        UUID ownerId = UUID.fromString(req.getParameter("ownerId"));
        playlistService.delete(playlistId, ownerId);
    }
}
