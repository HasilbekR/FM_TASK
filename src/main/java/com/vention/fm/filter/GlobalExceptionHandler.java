package com.vention.fm.filter;

import com.vention.fm.exception.AuthenticationFailedException;
import com.vention.fm.exception.UniqueObjectException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import com.vention.fm.exception.DataNotFoundException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@WebFilter("/*")
public class GlobalExceptionHandler implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
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
        } catch (DataNotFoundException e){
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
}
