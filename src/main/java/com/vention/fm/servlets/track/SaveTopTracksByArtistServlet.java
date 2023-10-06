package com.vention.fm.servlets.track;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.filter.AdminVerifier;
import com.vention.fm.service.TrackService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebServlet(urlPatterns = "/track/save-top-tracks-by-artist")
public class SaveTopTracksByArtistServlet extends HttpServlet {
    private final TrackService trackService = new TrackService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        AdminVerifier.verifyAdmin(UUID.fromString(userId));

        String page = req.getParameter("page");
        if(page == null) page = "1";
        String artist = req.getParameter("artist");
        String encodedArtist = URLEncoder.encode(artist, StandardCharsets.UTF_8);
        String apiUrl = Utils.url("/url")+"/track/save-top-tracks-by-artist?artist="+encodedArtist+"&page="+page;
        SaveTopTracksServlet.getTracks(resp, apiUrl, objectMapper, trackService);
    }
}
