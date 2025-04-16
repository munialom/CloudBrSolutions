package com.ctecx.brsuite.logins;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
public class LoginController {

/*
    @GetMapping("/login")
    public String loginPage(){

        return "login/login";
    }
*/

    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(value = "error", required = false) String error) {

        if (error != null) {
            try {
                // URLDecode the error message coming from CustomAuthenticationFailureHandler
                String decodedError = URLDecoder.decode(error, StandardCharsets.UTF_8);
                model.addAttribute("errorMessage", decodedError);
            } catch (Exception e) {
                // Fallback in case of decoding issues
                model.addAttribute("errorMessage", "Authentication failed");
            }
        }

        return "login/login";
    }

    @PostMapping("/waiter/login")
    public void waiterLogin(@RequestParam("pin") String pin, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // The actual authentication will be handled by Spring Security
    }


}