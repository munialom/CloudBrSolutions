package com.ctecx.brsuite.security;

import com.ctecx.brsuite.users.User;
import com.ctecx.brsuite.users.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PmsDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(PmsDetailsService.class);

    private final UserRepository userRepository;

    public PmsDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by email: {}", email);
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            logger.error("User not found with email: {}", email);
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return new PmsUserDetails(user);
    }

    public UserDetails loadUserByPin(String pin) throws UsernameNotFoundException {
        logger.debug("Attempting to load user by PIN");
        User user = userRepository.getUserByPin(pin);
        if (user == null || !user.hasRole("Waiter")) {
            logger.error("Could not find waiter with PIN");
            throw new UsernameNotFoundException("Could not find waiter with PIN");
        }
        return new PmsUserDetails(user);
    }



    public void updateLastLoginDate(String username) {
        logger.debug("Updating last login date for user: {}", username);
        User user = userRepository.getUserByEmail(username);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
        } else {
            logger.error("Failed to update last login date. User not found: {}", username);
        }
    }
}