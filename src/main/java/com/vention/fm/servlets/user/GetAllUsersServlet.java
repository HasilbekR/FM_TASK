package com.vention.fm.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.user.UserDto;
import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.UserService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/user/get-all-active-users")
public class GetAllUsersServlet extends HttpServlet {
    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<UserDto> allActiveUsers = userService.getAllActiveUsers();
            String json = objectMapper.writeValueAsString(allActiveUsers);
            resp.getWriter().print(json);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
