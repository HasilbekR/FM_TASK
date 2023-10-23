package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.album.AlbumRemoveTrackDto;
import com.vention.fm.service.AlbumTracksService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
@WebServlet(urlPatterns = "/album/remove-track")
public class RemoveTrackFromAlbumServlet extends HttpServlet {
    private final AlbumTracksService albumTracksService = new AlbumTracksService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        AlbumRemoveTrackDto albumTrack = objectMapper.readValue(req.getReader(), AlbumRemoveTrackDto.class);
        String result = albumTracksService.removeTrack(albumTrack);
        resp.getWriter().print(result);
    }
}
