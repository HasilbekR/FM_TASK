package com.vention.fm.servlets.album;

import com.vention.fm.service.AlbumService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/album/delete")
public class DeleteAlbumServlet extends HttpServlet {
    private final AlbumService albumService = new AlbumService();
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String albumId = req.getParameter("albumId");
        String ownerId = req.getParameter("ownerId");
        albumService.delete(albumId, ownerId);
    }
}
