package com.vention.fm.servlets.track;

import com.vention.fm.service.TrackService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/track/unblock")
public class UnblockTrackServlet extends HttpServlet {
    private final TrackService trackService = new TrackService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String trackName = req.getParameter("name");
        trackService.blockTrack(false, trackName);
        resp.getWriter().print("Track with name " + trackName + " is unblocked");
    }
}
