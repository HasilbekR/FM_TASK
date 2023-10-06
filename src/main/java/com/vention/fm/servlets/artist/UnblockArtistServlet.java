package com.vention.fm.servlets.artist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.artist.ArtistBlockDto;
import com.vention.fm.filter.AdminVerifier;
import com.vention.fm.service.ArtistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/artist/unblock")
public class UnblockArtistServlet extends HttpServlet {
    private final ArtistService artistService = new ArtistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArtistBlockDto artistBlockDto = objectMapper.readValue(req.getReader(), ArtistBlockDto.class);
        AdminVerifier.verifyAdmin(artistBlockDto.getAdminId());
        artistService.blockArtist(false, artistBlockDto.getArtistId());
    }
}
