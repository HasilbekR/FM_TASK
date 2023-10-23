package com.vention.fm.servlets.track;

import com.vention.fm.service.TrackService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/track/block")
public class BlockTrackServlet extends HttpServlet {
    private final TrackService trackService = new TrackService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String trackName = req.getParameter("name");
        trackService.blockTrack(true, trackName);
        resp.getWriter().print("Track with name " + trackName + " is blocked");
    }
}
