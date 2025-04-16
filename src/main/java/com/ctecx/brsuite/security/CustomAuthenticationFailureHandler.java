/*
package com.ctecx.brsuite.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "User not found";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Incorrect credentials";
        } else {
            errorMessage = "Authentication failed: " + exception.getMessage();
        }

        logger.error(errorMessage, exception);

        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        String redirectUrl;

        if (request.getRequestURI().contains("/waiter")) {
            redirectUrl = "/login?error=" + encodedErrorMessage;
        } else {
            redirectUrl = "/login?error=" + encodedErrorMessage;
        }

        response.sendRedirect(redirectUrl);
    }
}*/


package com.ctecx.brsuite.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "User not found";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Incorrect credentials";
        } else {
            errorMessage = "Authentication failed: " + exception.getMessage();
        }

        // Log the error message without the exception object to avoid stack trace
        //logger.error(errorMessage);

        String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        String redirectUrl;

        if (request.getRequestURI().contains("/waiter")) {
            redirectUrl = "/login?error=" + encodedErrorMessage;
        } else {
            redirectUrl = "/login?error=" + encodedErrorMessage;
        }

        response.sendRedirect(redirectUrl);
    }
}