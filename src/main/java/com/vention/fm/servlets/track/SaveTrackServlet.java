package com.vention.fm.servlets.track;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.track.TrackSaveDto;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.service.TrackService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/track/save")
public class SaveTrackServlet extends HttpServlet {

    private final TrackService trackService = new TrackService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        TrackSaveDto trackDto = objectMapper.readValue(req.getReader(), TrackSaveDto.class);
        Track savedTrack = trackService.createTrack(trackDto);
        String json = objectMapper.writeValueAsString(savedTrack);
        resp.getWriter().print(json);
    }
}
