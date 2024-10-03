package com.ctecx.brsuite.users;

import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("api-user")
public class UserRestController {
    private final UserService userService;

    @PostMapping("/users/check_mail")
    public String checkDuplicateEmail(@Param("email") String email) {


        return userService.isEmailUnique(email) ? "OK" : "Duplicate Email,Mail Already Registered";
    }

    @PostMapping("/users/update")
    public ResponseEntity<String> updateUserStatus(@RequestBody UserStatusDTO userStatusDTO) {
        try {
            userService.updateUserStatus(userStatusDTO);
            return new ResponseEntity<>("User status updated successfully.", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Failed to update user status. User not found.", HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateAccount(userDTO);

        if(updatedUser != null) {
            return ResponseEntity.ok("User updated successfully");
        } else {
            return ResponseEntity.status(500).body("Error updating user");
        }
    }

}



