package com.ctecx.brsuite.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class PinAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger logger = LoggerFactory.getLogger(PinAuthenticationFilter.class);

    public PinAuthenticationFilter() {
        setUsernameParameter("pin");
        setPasswordParameter("pin");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String pin = obtainUsername(request);
        logger.debug("Attempting authentication with PIN: {}", pin);
        pin = (pin != null) ? pin.trim() : "";
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(pin, pin);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        logger.debug("Successful authentication in PinAuthenticationFilter");

        SecurityContextHolder.getContext().setAuthentication(authResult);

        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        AuthenticationSuccessHandler successHandler = getSuccessHandler();
        if (successHandler != null) {
            logger.debug("Using custom success handler");
            successHandler.onAuthenticationSuccess(request, response, authResult);
        } else {
            logger.debug("No custom success handler, using default behavior");
            super.successfulAuthentication(request, response, chain, authResult);
        }

        logger.debug("Authentication process completed");
    }
}