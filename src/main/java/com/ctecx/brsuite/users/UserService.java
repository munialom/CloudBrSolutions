package com.ctecx.brsuite.users;


import com.ctecx.brsuite.userroles.Role;
import com.ctecx.brsuite.userroles.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void updateUserStatus(UserStatusDTO userStatusDTO) {
        User user = userRepository.findById(userStatusDTO.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userStatusDTO.getId()));

        if ("Activate".equals(userStatusDTO.getClicked())) {
            user.setEnabled(true);
        } else if ("Deactivate".equals(userStatusDTO.getClicked())) {
            user.setEnabled(false);
        }

        userRepository.save(user);
    }
    public String createUser(User user) {
        String password = generateRandomPassword(6);
      String newPass=passwordEncoder.encode(password);
       user.setPassword(newPass);

        userRepository.save(user);

        return password;

    }


    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
        boolean isVerified = false;

        if (user != null && !user.isEnabled()) {
            userRepository.enable(user.getId());

            isVerified = true;
        }

        return isVerified;
    }
    public Boolean checkFirstLogin(String email) {
        return userRepository.isFirstLogin(email);
    }




    public List<User> userList() {

        return (List<User>) userRepository.findAll();
    }

    public List<Role> roleList() {
        return (List<Role>) roleRepository.findAll();
    }

    private void encodePassword(User user) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }



    public User getCurrentUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String email = ((UserDetails)principal).getUsername();
            return userRepository.getUserByEmail(email);
        }

        return null; // or throw an exception
    }
    public User updateAccount(UserDTO userDTO) {
        User userInDB = userRepository.findById(userDTO.getId()).get();

        if (!userDTO.getPassword().isEmpty()) {
            userInDB.setPassword(userDTO.getPassword());
            encodePassword(userInDB);
        }

        userInDB.setFirstName(userDTO.getFirstName());
        userInDB.setLastName(userDTO.getLastName());
        userInDB.setFirstLogin(true);

        return userRepository.save(userInDB);
    }

    public User getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public boolean isEmailUnique(String email) {
      User  userBymail=  userRepository.getUserByEmail(email);

      return userBymail==null;
    }

    public static String generateRandomPassword(int len) {
        // define the characters that can be used in the password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        // create a Random object
        Random rnd = new Random();
        // create a StringBuilder to store the password
        StringBuilder sb = new StringBuilder(len);
        // loop until the password reaches the desired length
        for (int i = 0; i < len; i++) {
            // choose a random character from the chars string
            char c = chars.charAt(rnd.nextInt(chars.length()));
            // append the character to the password
            sb.append(c);
        }
        // return the password as a string
        return sb.toString();
    }
}
