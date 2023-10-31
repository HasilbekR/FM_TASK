package com.vention.fm.servlets.artist;

import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.ArtistService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/artist/unblock", "/artist/save-top-artists"})
public class ArtistUnblockAndSaveTopServlet extends HttpServlet {
    private final ArtistService artistService = new ArtistService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String artistName = req.getParameter("name");
            artistService.blockArtist(false, artistName);
            resp.getWriter().print("Artist with name " + artistName + " is unblocked");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String page = req.getParameter("page");
            String json = artistService.saveTopArtists(page);
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}