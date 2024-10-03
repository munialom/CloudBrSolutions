package com.ctecx.brsuite.util;

import com.ctecx.brsuite.branch.Branch;
import com.ctecx.brsuite.users.User;
import com.ctecx.brsuite.users.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    private static UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        SecurityUtils.userRepository = userRepository;
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UserNotFoundException("No authenticated user found");
        }

        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            email = (String) principal;
        } else {
            throw new UserNotFoundException("Unexpected principal type");
        }


        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new UserNotFoundException("User not found: " + email);
        }
        return user;
    }

    public static Branch getCurrentUserBranch() {
        User currentUser = getCurrentUser();
        return currentUser.getBranch();
    }

    public static Integer getCurrentUserId() {
        User currentUser = getCurrentUser();
        return currentUser.getId();
    }

    public static String getCurrentUserFullName() {
        User currentUser = getCurrentUser();
        return currentUser.getFullName();
    }
}