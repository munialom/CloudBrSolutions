package com.ctecx.brsuite.users;

import com.ctecx.brsuite.security.PmsUserDetails;
import com.ctecx.brsuite.systemsetup.AppSetupService;
import com.ctecx.brsuite.systemsetup.EmailSetting;
import com.ctecx.brsuite.util.Utility;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;


@Controller
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private  final UserService userService;
    private  final AppSetupService appSetupService;




    @PostMapping("/forgot-password")
    @ResponseBody
    public Map<String, Object> handleForgotPassword(
            @RequestParam("email") String email,
            HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        try {
            String newPassword = userService.generateAndUpdatePassword(email);
            sendPasswordResetEmail(request, email, newPassword);

            response.put("success", true);
            response.put("message", "Password has been reset. Please check your email.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to reset password. Please try again.");
        }

        return response;
    }

    @Async
    private void sendPasswordResetEmail(HttpServletRequest request, String email, String newPassword)
            throws MessagingException, UnsupportedEncodingException {
        // Get email settings
        EmailSetting emailSettings = appSetupService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        // Get user details
        User user = userService.getByEmail(email);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getSenderEmail(), emailSettings.getSenderName());
        helper.setTo(email);
        helper.setSubject("Your Password Has Been Reset");

        String loginURL = Utility.getSiteUrl(request) + "/login";

        // Create email content
        String content = """
            <p>Dear [[name]],</p>
            <p>Your password has been reset as requested.</p>
            <p>Your new password is: <strong>[[password]]</strong></p>
            <p>Please login using this password and change it immediately.</p>
            <p>Click here to login: <a href="[[URL]]">Login</a></p>
            <p>Thank you,<br>Your Application Team</p>
            """;

        content = content.replace("[[name]]", user.getFirstName());
        content = content.replace("[[password]]", newPassword);
        content = content.replace("[[URL]]", loginURL);

        helper.setText(content, true);

        mailSender.send(message);
    }

    @GetMapping
    public String registerUser(Model model){
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("user", user);
        model.addAttribute("rolesData",userService.roleList());
        model.addAttribute("usersList",userService.userList());


        return "configs/addUser";
    }


    @GetMapping("reset-password")
    public String reSetPassWord(Model model){



        return "users/passresetdata";
    }


    @GetMapping("profile")
    public String profileUser(@AuthenticationPrincipal PmsUserDetails loggedUser, Model model){
        String email = loggedUser.getUsername();
        User user = userService.getByEmail(email);

        model.addAttribute("user", user);

        return "users/profile";
    }




    @GetMapping("data")
    public String UsersData(Model model){
        User user = new User();
        user.setEnabled(true);
        model.addAttribute("usersList",userService.userList());


        return "users/users-table";
    }

    @GetMapping("/checkFirstLogin")
    public ResponseEntity<String> checkFirstLogin(@RequestParam String email) {
        Boolean isFirstLogin = userService.checkFirstLogin(email);
        if (isFirstLogin != null && !isFirstLogin) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/users/profile")).build();
        } else {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/members/data-view")).build();
        }
    }

 /*   @PostMapping
    public String saveUser(User user,	HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
      String createdUserPass =userService.createUser(user);

        sendVerificationEmail(request, user,createdUserPass);

        return "redirect:/";
    }*/

    @PostMapping
    @ResponseBody
    public Map<String, Object> saveUser(User user, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String createdUserPass = userService.createUser(user);
        sendVerificationEmail(request, user, createdUserPass);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "User created successfully");
        return response;
    }

    @GetMapping("/verify")
    public String verifyAccount(String code, Model model) {
        boolean verified = userService.verify(code);

        return "users/"+(verified ? "verify_sucess": "verify_fail");
    }



    @Async
    void sendVerificationEmail(HttpServletRequest request, User user, String password) throws MessagingException, UnsupportedEncodingException {
        // get the email settings from the app setup service
        EmailSetting emailSettings = appSetupService.getEmailSettings();

        // create a mail sender with the email settings
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        // get the user's email address
        String toAddress = user.getEmail();

        // get the email subject
        String subject = emailSettings.getSubject();

        // get the email content template
        String content = emailSettings.getContent();

        // create a mime message
        MimeMessage message = mailSender.createMimeMessage();

        // create a message helper to set the message properties
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);

        // set the sender's email and name
        messageHelper.setFrom(emailSettings.getSenderEmail(), emailSettings.getSenderName());

        // set the recipient's email
        messageHelper.setTo(toAddress);

        // set the subject
        messageHelper.setSubject(subject);

        // generate login URL
        String loginURL = Utility. getSiteUrl(request) + "/login";

        // replace the placeholders in the content with the user's name, username, password, and login link
        content = content.replace("[[name]]", user.getFirstName());
        content = content.replace("[[username]]", user.getEmail());
        content = content.replace("[[password]]", password);
        content = content.replace("[[URL]]", loginURL);

        // set the content as HTML
        messageHelper.setText(content, true);

        // send the message
        mailSender.send(message);
    }





}
