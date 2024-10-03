package com.ctecx.brsuite.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LastLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(LastLoginSuccessHandler.class);

    private final PmsDetailsService userDetailsService;

    public LastLoginSuccessHandler(PmsDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
        setAlwaysUseDefaultTargetUrl(false);
        setUseReferer(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.debug("Authentication success. Principal: {}", authentication.getPrincipal());

        if (authentication.getPrincipal() instanceof PmsUserDetails) {
            PmsUserDetails userDetails = (PmsUserDetails) authentication.getPrincipal();
            String username = userDetails.getUsername();
            logger.debug("Updating last login date for user: {}", username);
            userDetailsService.updateLastLoginDate(username);
        }

        String targetUrl = determineTargetUrl(authentication);
        logger.debug("Determined target URL: {}", targetUrl);

        clearAuthenticationAttributes(request);

        // Set the authentication in the session
        HttpSession session = request.getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        logger.debug("Redirecting to: {}", targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(Authentication authentication) {
        boolean isWaiter = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("Waiter"));
        logger.debug("Is user a Waiter? {}", isWaiter);
        if (isWaiter) {
            return "/waiter/dashboard";
        } else {
            return "/";
        }
    }
}