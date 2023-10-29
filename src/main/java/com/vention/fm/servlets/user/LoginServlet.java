package com.vention.fm.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.user.LoginDto;
import com.vention.fm.domain.dto.user.UserDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.UserService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/user/sign-in")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            LoginDto loginDto = objectMapper.readValue(req.getReader(), LoginDto.class);
            UserDto user = userService.signIn(loginDto);
            String json = objectMapper.writeValueAsString(user);
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
