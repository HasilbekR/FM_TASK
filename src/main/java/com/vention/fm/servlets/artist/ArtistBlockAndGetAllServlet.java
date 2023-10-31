package com.vention.fm.servlets.artist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.ArtistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"/artist/block", "/artist/get-all"})
public class ArtistBlockAndGetAllServlet extends HttpServlet {
    private final ArtistService artistService = new ArtistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String artistName = req.getParameter("name");
            artistService.blockArtist(true, artistName);
            resp.getWriter().print("Artist with name " + artistName + " is blocked");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String json = objectMapper.writeValueAsString(artistService.getAll());
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
