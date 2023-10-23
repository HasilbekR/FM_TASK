package com.vention.fm.servlets.artist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.artist.ArtistSaveDto;
import com.vention.fm.domain.model.artist.Artist;
import com.vention.fm.service.ArtistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/artist/save")
public class SaveArtistServlet extends HttpServlet {

    private final ArtistService artistService = new ArtistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ArtistSaveDto artistDto = objectMapper.readValue(req.getReader(), ArtistSaveDto.class);
        Artist artist = Artist
                .builder()
                .name(artistDto.getName())
                .url(artistDto.getUrl())
                .build();
        artistService.save(artist);
    }
}
