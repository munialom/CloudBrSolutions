package com.ctecx.brsuite.products;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomProductService {
    private final ProductRepository productRepository;

    public List<Map<String, Object>> getAllProducts() {
        return productRepository.getAllProducts();
    }
}
