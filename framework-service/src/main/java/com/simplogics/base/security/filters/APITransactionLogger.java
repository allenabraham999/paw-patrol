package com.simplogics.base.security.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(2)
public class APITransactionLogger implements Filter {

    private final static Logger LOG = LoggerFactory.getLogger(APITransactionLogger.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        LOG.info("API Request  {} : {} ", req.getMethod(), req.getRequestURI());
        chain.doFilter(request, response);
        LOG.info("API Response :{} ", res.getContentType());
    }

    @Override
    public void destroy() {

    }
}
