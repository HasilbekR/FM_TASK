package com.vention.fm.servlets.track;

import com.vention.fm.service.TrackService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/track/save-top-tracks-by-artist")
public class SaveTopTracksByArtistServlet extends HttpServlet {
    private final TrackService trackService = new TrackService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String page = req.getParameter("page");
        String artist = req.getParameter("artist");
        String tracks = trackService.saveTopTracksByArtist(artist, page);
        resp.getWriter().print(tracks);
    }
}
