package com.vention.fm.servlets.user;

import com.vention.fm.exception.BadRequestException;
import com.vention.fm.service.UserService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/user/block")
public class BlockUserServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String username = req.getParameter("username");
            userService.blockUser(true, username);
            resp.getWriter().print("User with username " + username + " is blocked");
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
