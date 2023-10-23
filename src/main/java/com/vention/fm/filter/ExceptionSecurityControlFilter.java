package com.vention.fm.filter;

import com.vention.fm.exception.AuthenticationFailedException;
import com.vention.fm.exception.UniqueObjectException;
import com.vention.fm.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.vention.fm.exception.DataNotFoundException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@WebFilter("/*")
public class ExceptionSecurityControlFilter implements Filter {
    private static final UserService userService = new UserService();
    private final List<String> RESTRICTED_URLS = Arrays.asList(
            "http://localhost:8081/artist/block",
            "http://localhost:8081/artist/unblock",
            "http://localhost:8081/artist/save-top-artists",
            "http://localhost:8081/track/block",
            "http://localhost:8081/track/unblock",
            "http://localhost:8081/track/save-top-tracks",
            "http://localhost:8081/track/save-top-tracks-by-artist",
            "http://localhost:8081/user/block",
            "http://localhost:8081/user/unblock",
            "http://localhost:8081/user/get-all-blocked-users",
            "http://localhost:8081/user/get-all-active-users"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String pathInfo = httpRequest.getRequestURL().toString();

        try {
            if (RESTRICTED_URLS.contains(pathInfo)) {
                verifyAdmin(UUID.fromString(httpRequest.getParameter("userId")));
            }
            chain.doFilter(request, response);
        } catch (AuthenticationFailedException | AccessDeniedException e) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error message", e.getMessage());

            response.getWriter().write(jsonResponse.toString());
        } catch (UniqueObjectException e) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("application/json");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error message", e.getMessage());

            response.getWriter().write(jsonResponse.toString());
        } catch (DataNotFoundException e) {
            httpResponse.setStatus(HttpServletResponse.SC_NOT_FOUND);
            httpResponse.setContentType("application/json");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error message", e.getMessage());

            response.getWriter().write(jsonResponse.toString());
        }

    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private static void verifyAdmin(UUID adminId) throws AccessDeniedException {
        String userRole = userService.getUserRole(adminId);
        if (!Objects.equals(userRole, "ADMINISTRATOR")) throw new AccessDeniedException("Access denied");
    }
}
