package com.vention.fm.servlets.track;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.track.TrackDto;
import com.vention.fm.domain.dto.track.TrackSaveDto;
import com.vention.fm.exception.BadRequestException;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            TrackSaveDto tracksaveDto = objectMapper.readValue(req.getReader(), TrackSaveDto.class);
            TrackDto track = trackService.createTrack(tracksaveDto);
            String json = objectMapper.writeValueAsString(track);
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
