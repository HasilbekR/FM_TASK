package com.vention.fm.servlets.artist;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.filter.AdminVerifier;
import com.vention.fm.service.ArtistService;
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

@WebServlet(urlPatterns = "/artist/save-top-artists")
public class SaveTopArtistsServlet extends HttpServlet {

    private final ArtistService artistService = new ArtistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("userId");
        AdminVerifier.verifyAdmin(UUID.fromString(userId));

        String page = req.getParameter("page");
        if(page == null) page = "1";
        String apiUrl = Utils.url("/url")+"/artist/save-top-artists?page="+page;
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("service", "main");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                List<Artist> artists = objectMapper.readValue(reader, new TypeReference<>() {});
                List<Artist> savedArtists = artistService.saveAll(artists);
                String json = objectMapper.writeValueAsString(savedArtists);
                resp.getWriter().print(json);
            }
        }
    }
}
