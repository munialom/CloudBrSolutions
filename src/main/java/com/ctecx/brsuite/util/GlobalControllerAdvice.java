package com.ctecx.brsuite.util;

import com.ctecx.brsuite.security.PmsUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import java.util.Collection;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUser")
    public String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof PmsUserDetails) {
            PmsUserDetails userDetails = (PmsUserDetails) auth.getPrincipal();
            return userDetails.getFullName();
        }
        return "Anonymous";
    }

    @ModelAttribute("userRoles")
    public Collection<String> getUserRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @ModelAttribute("userBranch")
    public String getUserBranch() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof PmsUserDetails userDetails) {
            return userDetails.getBranchName();
        }
        return null;
    }
}