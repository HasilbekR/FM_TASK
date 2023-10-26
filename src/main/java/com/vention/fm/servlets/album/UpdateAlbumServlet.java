package com.vention.fm.servlets.album;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.album.AlbumUpdateDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.AlbumService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/album/update")
public class UpdateAlbumServlet extends HttpServlet {
    private final AlbumService albumService = new AlbumService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            AlbumUpdateDto albumDto = objectMapper.readValue(req.getReader(), AlbumUpdateDto.class);
            String result = albumService.update(albumDto);
            resp.getWriter().print(result);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

}
