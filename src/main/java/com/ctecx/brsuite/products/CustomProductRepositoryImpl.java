package com.ctecx.brsuite.products;


import com.ctecx.brsuite.util.SecurityUtils;
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
        Integer branchId = Math.toIntExact(SecurityUtils.getCurrentUserBranch().getId());
        String sql = "CALL GetProductDetails(?)";
        return jdbcTemplate.queryForList(sql,branchId);
    }

    @Override
    public List<Map<String, Object>> searchProductsWithPositiveStock(String searchKey, int pageSize, int offset) {
        Integer branchId = Math.toIntExact(SecurityUtils.getCurrentUserBranch().getId());
        String sql = "CALL search_products_with_positive_stock(?, ?, ?,?)";
        return jdbcTemplate.queryForList(sql, searchKey, pageSize, offset,branchId);
    }

    @Override
    public List<Map<String, Object>> GetCookingGasStockLevels() {
        String sql = "CALL GetCookingGasStockLevels()";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> GetBelowLowStockLevels(int branchId) {
        String sql = "CALL GetBelowLowStockLevels(?)";
        return jdbcTemplate.queryForList(sql,branchId);
    }

    @Override
    public List<Map<String, Object>> GetLowStockLevels() {
        String sql = "CALL GetLowStockLevels()";
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public Map<String, Object> search_product_by_code(String productCode) {
        Integer branchId = Math.toIntExact(SecurityUtils.getCurrentUserBranch().getId());
        String sql = "CALL search_products_by_code(?,?)";
        return jdbcTemplate.queryForMap(sql, productCode,branchId);
    }

/*    @Override
    public Map<String, Object> search_product_by_code_all(String productCode) {
        String sql = "CALL search_products_by_code_all(?)";
        return jdbcTemplate.queryForMap(sql, productCode);
    }


    @Override
    public List<Map<String, Object>> searchProductsWithPositiveStock_all(String searchKey, int pageSize, int offset) {
        String sql = "CALL search_products_with_positive_stock_all(?, ?, ?)";
        return jdbcTemplate.queryForList(sql, searchKey, pageSize, offset);
    }*/

    @Override
    public Map<String, Object> search_product_by_code_all(String productCode) {
        Integer branchId = Math.toIntExact(SecurityUtils.getCurrentUserBranch().getId());
        String sql = "CALL search_products_by_code_all(?, ?)";
        return jdbcTemplate.queryForMap(sql, productCode, branchId);
    }


    @Override
    public List<Map<String, Object>> searchProductsWithPositiveStock_all(String searchKey, int pageSize, int offset) {
        Integer branchId = Math.toIntExact(SecurityUtils.getCurrentUserBranch().getId());
        String sql = "CALL search_products_with_positive_stock_all(?, ?, ?, ?)";
        return jdbcTemplate.queryForList(sql, searchKey, pageSize, offset, branchId);
    }

}
