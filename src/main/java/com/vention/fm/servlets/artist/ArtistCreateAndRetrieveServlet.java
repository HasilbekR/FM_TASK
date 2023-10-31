package com.vention.fm.servlets.artist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.artist.ArtistDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.ArtistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/artist/save", "/artist/get-by-name"})
public class ArtistCreateAndRetrieveServlet extends HttpServlet {

    private final ArtistService artistService = new ArtistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ArtistDto artistDto = objectMapper.readValue(req.getReader(), ArtistDto.class);
            ArtistDto artist = artistService.create(artistDto);
            resp.getWriter().print(objectMapper.writeValueAsString(artist));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String name = req.getParameter("name");
            String json = objectMapper.writeValueAsString(artistService.getArtistDto(name));
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
