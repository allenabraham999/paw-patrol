package com.simplogics.base.security.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;

@Component
@Order(1)
public class CORSFilter implements Filter {
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");

        HttpServletRequest request = (HttpServletRequest) req;
        if ("options".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(200);
            Writer writer = response.getWriter();
            response.getWriter().write("");
            writer.close();
            return;
        }

        // Set cache control for static assets
        if (!request.getRequestURL().toString().contains("/api")
                && request.getServletContext().getMimeType(request.getRequestURL().toString()) != "text/html") {
            response.setHeader("Cache-Control", "public, max-age=86400000");
        }
        chain.doFilter(req, res);
    }

    public void init(FilterConfig filterConfig) {
    }

    public void destroy() {
    }

}
