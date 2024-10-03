package com.ctecx.brsuite.productcategory;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
@RequiredArgsConstructor
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;



    @Override
    public List<Map<String, Object>> loadWaiterMenuCategories() {
        return jdbcTemplate.queryForList("CALL GetWaiterMenuCategories()");
    }


    @Override
    public List<Map<String, Object>> loadProductsByMenuCategories(int categoryID) {
        return jdbcTemplate.queryForList("CALL  GetProductDetailsByCategory(?)", categoryID);
    }
}
