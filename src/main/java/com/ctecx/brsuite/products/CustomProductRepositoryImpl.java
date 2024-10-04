package com.ctecx.brsuite.products;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class CustomProductRepositoryImpl implements CustomProductRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getAllProducts() {
        return jdbcTemplate.queryForList("CALL GetProductDetails()");
    }

    @Override
    public List<Map<String, Object>> searchProductsWithPositiveStock(String searchKey, int pageSize, int offset) {
        String sql = "CALL search_products_with_positive_stock(?, ?, ?)";
        return jdbcTemplate.queryForList(sql, searchKey, pageSize, offset);
    }
}
