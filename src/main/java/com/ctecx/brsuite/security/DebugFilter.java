package com.ctecx.brsuite.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class DebugFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(DebugFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Before processing - Request URL: {}", request.getRequestURL());
        logger.debug("Before processing - Request Method: {}", request.getMethod());
        logger.debug("Before processing - Request URI: {}", request.getRequestURI());
        logger.debug("Before processing - Authentication: {}", SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request, response);

        logger.debug("After processing - Authentication: {}", SecurityContextHolder.getContext().getAuthentication());
        logger.debug("After processing - Response Status: {}", response.getStatus());
    }
}