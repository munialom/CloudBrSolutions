package com.ctecx.brsuite.revenue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevenueService {
    private final RevenueRepository revenueRepository;

    @Autowired
    public RevenueService(RevenueRepository revenueRepository) {
        this.revenueRepository = revenueRepository;
    }
    public Revenue findByRevenueName(String revenueName) {
        return revenueRepository.findByRevenueName(revenueName)
                .orElseThrow(() -> new RuntimeException("Revenue not found: " + revenueName));
    }
    public Revenue save(Revenue revenue) {
        return revenueRepository.save(revenue);
    }

    public Revenue get(Long id) {
        return revenueRepository.findById(id).orElse(null);
    }

    public List<Revenue> revenueList(){
        return revenueRepository.findAll();
    }


    public List<Revenue> searchRevenues(String keyword) {
        return revenueRepository.findByRevenueNameContainingIgnoreCase(keyword);
    }

    public List<Revenue> getAllRevenueCodes() {
        return revenueRepository.findAll();
    }

    public Revenue createRevenueCode(Revenue revenueCode) {
        return revenueRepository.save(revenueCode);
    }
}
