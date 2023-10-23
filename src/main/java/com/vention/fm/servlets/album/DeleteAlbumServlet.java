package com.vention.fm.servlets.album;

import com.vention.fm.service.AlbumService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/album/delete")
public class DeleteAlbumServlet extends HttpServlet {
    private final AlbumService albumService = new AlbumService();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String albumName = req.getParameter("name");
        String ownerId = req.getParameter("userId");
        String result = albumService.delete(albumName, ownerId);
        resp.getWriter().print(result);
    }
}
