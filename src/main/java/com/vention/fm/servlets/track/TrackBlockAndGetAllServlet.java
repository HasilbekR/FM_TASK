package com.vention.fm.servlets.track;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.TrackService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/track/block", "/track/get-all"})
public class TrackBlockAndGetAllServlet extends HttpServlet {
    private final TrackService trackService = new TrackService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String trackName = req.getParameter("name");
            trackService.blockTrack(true, trackName);
            resp.getWriter().print("Track with name " + trackName + " is blocked");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = objectMapper.writeValueAsString(trackService.getAll());
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
