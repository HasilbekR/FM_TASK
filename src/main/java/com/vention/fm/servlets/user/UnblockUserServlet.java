package com.vention.fm.servlets.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.fm.domain.dto.user.UserBlockDto;
import com.vention.fm.filter.AdminVerifier;
import com.vention.fm.service.UserService;
import com.vention.fm.utils.Utils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.UUID;

@WebServlet(urlPatterns = "/user/unblock")
public class UnblockUserServlet extends HttpServlet {
    private final ObjectMapper objectMapper = Utils.getObjectMapper();
    private final UserService userService = new UserService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserBlockDto userBlockDto = objectMapper.readValue(req.getReader(), UserBlockDto.class);
        UUID adminId = userBlockDto.getAdminId();
        AdminVerifier.verifyAdmin(adminId);

        userService.blockUser(false, userBlockDto.getUserId());
    }
}
