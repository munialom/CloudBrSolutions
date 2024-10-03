package com.ctecx.brsuite.util;


import com.ctecx.brsuite.security.PmsUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        if (authentication.getPrincipal() instanceof PmsUserDetails userDetails) {
            return Optional.ofNullable(userDetails.getFullName());
        }

        // Fallback to username if PmsUserDetails is not available
        return Optional.of(authentication.getName());
    }
}