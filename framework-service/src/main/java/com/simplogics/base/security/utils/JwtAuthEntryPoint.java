package com.simplogics.base.security.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplogics.base.dto.BaseResponse;
import com.simplogics.base.utils.Translator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        logger.error("Unauthorized error: {}", authException.getMessage());
        List<String> errors = new ArrayList<>();
        errors.add(authException.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getOutputStream().println(new ObjectMapper().writeValueAsString(BaseResponse.builder()
                .status(false)
                .messageCode(1)
                .data(null)
                .hasErrors(true)
                .message(Translator.translateToLocale("unauthorized.error"))
                .errors(errors)
                .build()));
    }
}
