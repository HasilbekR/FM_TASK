package com.vention.fm.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.model.user.UserEntity;
import com.vention.fm.filter.AdminVerifier;
import com.vention.fm.service.UserService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet(urlPatterns = "/user/get-all-blocked-users")
public class GetAllBlockedUsers extends HttpServlet {
    private final UserService userService = new UserService();
    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String adminId = req.getParameter("adminId");
        AdminVerifier.verifyAdmin(UUID.fromString(adminId));

        List<UserEntity> allBlockedUsers = userService.getAllBlockedUsers();
        String json = objectMapper.writeValueAsString(allBlockedUsers);
        resp.getWriter().print(json);
    }
}
