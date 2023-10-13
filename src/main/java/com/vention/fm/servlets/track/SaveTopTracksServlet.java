package com.vention.fm.servlets.track;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.model.track.Track;
import com.vention.fm.filter.AdminVerifier;
import com.vention.fm.service.TrackService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/track/save-top-tracks")
public class SaveTopTracksServlet extends HttpServlet {

    private final TrackService trackService = new TrackService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        AdminVerifier.verifyAdmin(UUID.fromString(userId));

        String page = req.getParameter("page");
        if (page == null) page = "1";
        String apiUrl = Utils.url("/url") + "/track/save-top-tracks?page=" + page;
        getTracks(resp, apiUrl, objectMapper, trackService);
    }

    static void getTracks(HttpServletResponse resp, String apiUrl, ObjectMapper objectMapper, TrackService trackService) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("service", "main");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                List<Track> tracks = objectMapper.readValue(reader, new TypeReference<>() {
                });
                List<Track> savedTracks = trackService.saveAll(tracks);
                String json = objectMapper.writeValueAsString(savedTracks);
                resp.getWriter().print(json);
            }
        }
    }
}
