package com.ctecx.brsuite.products;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomProductService {
    private final ProductRepository productRepository;

    public List<Map<String, Object>> getAllProducts() {
        return productRepository.getAllProducts();
    }



    public Page<ProductSaleDTO> salesByProductNameAndCode(String searchKey, int pageNumber, int pageSize) {
        List<Map<String, Object>> results = productRepository.searchProductsWithPositiveStock(
                searchKey,
                pageSize,
                pageNumber * pageSize
        );

        List<ProductSaleDTO> dtoList = results.stream()
                .map(this::mapToProductSaleDTO)
                .collect(Collectors.toList());

        long totalCount = 0;
        if (!results.isEmpty()) {
            totalCount = ((Number) results.get(0).get("total_count")).longValue();
        }

        return new PageImpl<>(dtoList, PageRequest.of(pageNumber, pageSize), totalCount);
    }

    private ProductSaleDTO mapToProductSaleDTO(Map<String, Object> row) {
        return new ProductSaleDTO(
                (String) row.get("product_name"),
                (String) row.get("product_code"),
                ((Number) row.get("product_cost")).doubleValue(),
                ((Number) row.get("product_price")).doubleValue(),
                ((Number) row.get("current_stock")).intValue(),
                ((Number) row.get("tax")).doubleValue()
        );
    }
}
