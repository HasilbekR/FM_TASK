package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.domain.dto.LoginDto;
import org.example.domain.dto.UserRequestDto;
import org.example.domain.model.user.UserEntity;
import org.example.service.UserService;

import java.io.IOException;

@WebServlet(urlPatterns = {"/auth/*"})
public class AuthController extends HttpServlet {
    private final UserService userService = new UserService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestUrl = req.getPathInfo();
        switch (requestUrl){
            case "/sign-in" -> {
                ObjectMapper objectMapper = new ObjectMapper();
                LoginDto loginDto = objectMapper.readValue(req.getReader(), LoginDto.class);
                UserEntity userEntity = userService.signIn(loginDto);
                resp.getWriter().print(userEntity.toString());
            }
            case "/sign-up" -> {
                ObjectMapper objectMapper = new ObjectMapper();
                UserRequestDto userRequestDto = objectMapper.readValue(req.getReader(), UserRequestDto.class);
                userService.signUp(userRequestDto);
                resp.getWriter().print("Successfully signed up");
            }
        }
    }
}
