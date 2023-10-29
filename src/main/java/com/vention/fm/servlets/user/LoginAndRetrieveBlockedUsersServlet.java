package com.vention.fm.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.user.UserRequestDto;
import com.vention.fm.domain.dto.user.UserResponseDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.UserService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/user/sign-in", "/user/get-all-blocked-users"})
public class LoginAndRetrieveBlockedUsersServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            UserRequestDto loginDto = objectMapper.readValue(req.getReader(), UserRequestDto.class);
            UserResponseDto user = userService.signIn(loginDto);
            String json = objectMapper.writeValueAsString(user);
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<UserResponseDto> allBlockedUsers = userService.getAllBlockedUsers();
            String json = objectMapper.writeValueAsString(allBlockedUsers);
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
