package com.ctecx.brsuite.revenue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/revenues")
public class RevenueController {
    private final RevenueService revenueService;



    @GetMapping(path = "/search", produces = "application/json")
    public List<Revenue> searchRevenues(@RequestParam String keyword) {
        return revenueService.searchRevenues(keyword);
    }


    @Autowired
    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Revenue revenue) {
        Map<String, Object> response = new HashMap<>();
        try {
            Revenue savedRevenue = revenueService.save(revenue);
            response.put("success", true);
            response.put("revenue", savedRevenue);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to save revenue data.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/{id}")
    public Revenue get(@PathVariable Long id) {
        return revenueService.get(id);
    }
}