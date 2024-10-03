package com.ctecx.brsuite.logins;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(){

        return "login/login";
    }


    @PostMapping("/waiter/login")
    public void waiterLogin(@RequestParam("pin") String pin, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // The actual authentication will be handled by Spring Security
    }


}