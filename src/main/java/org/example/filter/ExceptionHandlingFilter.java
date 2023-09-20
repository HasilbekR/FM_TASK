package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.example.exception.AuthenticationFailedException;
import org.example.exception.UniqueObjectException;
import org.json.JSONObject;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (AuthenticationFailedException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
            httpResponse.setContentType("application/json");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error", true);
            jsonResponse.put("message", e.getMessage());

            response.getWriter().write(jsonResponse.toString());
            request.setAttribute("errorMessage", e.getMessage());
        } catch (UniqueObjectException e) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.setContentType("application/json");

            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("error", true);
            jsonResponse.put("message", e.getMessage());

            response.getWriter().write(jsonResponse.toString());
            request.setAttribute("errorMessage", e.getMessage());
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
