package com.ctecx.brsuite.waiters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/waiter")
public class WaiterController {

    private static final Logger logger = LoggerFactory.getLogger(WaiterController.class);

    @GetMapping("/dashboard")
    public String waiterDashboard() {
        logger.debug("Waiter dashboard accessed");
        return "redirect:/waiter/dashboard-view";  // Redirect to the actual view
    }

    @GetMapping("/dashboard-view")
    public String waiterDashboardView() {
        logger.debug("Waiter dashboard view accessed");
        return "waiters/index";  // This is the name of your Thymeleaf template
    }
}