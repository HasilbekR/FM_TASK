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

@WebServlet(urlPatterns = "/track/get-by-artist")
public class GetByArtistServlet extends HttpServlet {
    private final TrackService trackService = new TrackService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String name = req.getParameter("name");
            String json = objectMapper.writeValueAsString(trackService.getTrackListByArtistName(name));
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        String method = req.getMethod();
        String requestURI = req.getRequestURI();
        if (method.equals("GET") && requestURI.equals("/track/get-by-artist")) {
            doGet(req, resp);
        } else {
            Utils.methodNotAllowed(req, resp);
        }
    }
}
