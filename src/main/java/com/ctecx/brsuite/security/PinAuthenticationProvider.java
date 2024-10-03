package com.ctecx.brsuite.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class PinAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(PinAuthenticationProvider.class);

    @Autowired
    private PmsDetailsService pmsDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String pin = authentication.getName();
        logger.debug("Attempting to authenticate with PIN: {}", pin);

        PmsUserDetails userDetails = (PmsUserDetails) pmsDetailsService.loadUserByPin(pin);

        if (userDetails == null) {
            logger.debug("No user found for PIN: {}", pin);
            throw new BadCredentialsException("Invalid PIN");
        }

        logger.debug("User found for PIN: {}. Authorities: {}", pin, userDetails.getAuthorities());

        // Ensure the user has the Waiter authority
        if (!userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("Waiter"))) {
            logger.debug("User does not have Waiter authority: {}", userDetails.getUsername());
            throw new BadCredentialsException("User is not authorized as a Waiter");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, pin, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}