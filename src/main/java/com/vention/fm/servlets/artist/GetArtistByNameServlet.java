package com.vention.fm.servlets.artist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.service.ArtistService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/artist/get-by-name")
public class GetArtistByNameServlet extends HttpServlet {
    private final ArtistService artistService = new ArtistService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("name");
        String json = objectMapper.writeValueAsString(artistService.getArtistByName(name));
        resp.getWriter().print(json);
    }
}
