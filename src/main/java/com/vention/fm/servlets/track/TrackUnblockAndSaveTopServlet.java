package com.vention.fm.servlets.track;

import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.TrackService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/track/unblock", "/track/save-top-tracks"})
public class TrackUnblockAndSaveTopServlet extends HttpServlet {
    private final TrackService trackService = new TrackService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String trackName = req.getParameter("name");
            trackService.blockTrack(false, trackName);
            resp.getWriter().print("Track with name " + trackName + " is unblocked");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String page = req.getParameter("page");
            String tracks = trackService.saveTopTracks(page);
            resp.getWriter().print(tracks);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
