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




    public boolean resetPassword(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            return false;
        }

        // Generate new random password
        String newPassword = generateRandomPassword(8);

        // Encode and update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLogin(false); // Optional: Force user to change password on next login

        userRepository.save(user);

        return true;
    }

    // This method returns the new password so it can be sent via email
    public String generateAndUpdatePassword(String email) {
        User user = userRepository.getUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        // Generate new random password
        String newPassword = generateRandomPassword(8);

        // Encode and update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setFirstLogin(false);

        userRepository.save(user);

        return newPassword;
    }

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
    public User updateAccount(UserData userDTO) {
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





    // Move UserDto to be a static nested class in UserService
    public static class UserDto {
        private Integer id;
        private String firstName;
        private String lastName;
        private String roleName;

        public UserDto(Integer id, String firstName, String lastName, String roleName) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.roleName = roleName;
        }

        // Getters
        public Integer getId() { return id; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getRoleName() { return roleName; }
    }
}
