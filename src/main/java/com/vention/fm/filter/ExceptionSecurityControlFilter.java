package com.vention.fm.filter;

import com.vention.fm.exception.*;
import com.vention.fm.service.UserService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@WebFilter("/*")
public class ExceptionSecurityControlFilter implements Filter {
    private static final UserService userService = new UserService();
    private final List<String> RESTRICTED_URLS = Arrays.asList(
            "/artist/block",
            "/artist/unblock",
            "/artist/save-top-artists",
            "/track/block",
            "/track/unblock",
            "/track/save-top-tracks",
            "/track/save-top-tracks-by-artist",
            "/user/block",
            "/user/unblock",
            "/user/get-all-blocked-users",
            "/user/get-all-active-users"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException{
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String pathInfo = httpRequest.getRequestURL().toString();

        try {
            boolean isRestricted = RESTRICTED_URLS.stream().anyMatch(pathInfo::contains);
            if (isRestricted) {
                verifyAdmin(UUID.fromString(httpRequest.getParameter("userId")));
            }
            chain.doFilter(request, response);
        } catch (AuthenticationFailedException | AccessRestrictedException e) {
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error message", e.getMessage());

            response.getWriter().write(jsonResponse.toString());
        } catch (UniqueObjectException | BadRequestException | ServletException e) {
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

    private static void verifyAdmin(UUID adminId) throws AccessRestrictedException {
        String userRole = userService.getUserRole(adminId);
        if (!Objects.equals(userRole, "ADMINISTRATOR")) throw new AccessRestrictedException("Access denied");
    }
}
