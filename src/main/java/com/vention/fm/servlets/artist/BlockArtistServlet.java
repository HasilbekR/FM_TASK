package com.vention.fm.servlets.artist;

import com.vention.fm.service.ArtistService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/artist/block")
public class BlockArtistServlet extends HttpServlet {
    private final ArtistService artistService = new ArtistService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String artistName = req.getParameter("name");
        artistService.blockArtist(true, artistName);
        resp.getWriter().print("Artist with name " + artistName + " is blocked");
    }
}
