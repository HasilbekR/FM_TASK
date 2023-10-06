package com.vention.fm.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.user.LoginDto;
import com.vention.fm.domain.model.user.UserEntity;
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        LoginDto loginDto = objectMapper.readValue(req.getReader(), LoginDto.class);
        UserEntity userEntity = userService.signIn(loginDto);
        String json = objectMapper.writeValueAsString(userEntity);
        resp.getWriter().print(json);
    }
}
