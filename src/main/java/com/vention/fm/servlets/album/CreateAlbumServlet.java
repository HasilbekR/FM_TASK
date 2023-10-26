package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.album.AlbumCreateDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.AlbumService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/album/save")
public class CreateAlbumServlet extends HttpServlet {

    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    private final AlbumService albumService = new AlbumService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp){
        try {
            AlbumCreateDto albumCreateDto = objectMapper.readValue(req.getReader(), AlbumCreateDto.class);
            String result = albumService.save(albumCreateDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
