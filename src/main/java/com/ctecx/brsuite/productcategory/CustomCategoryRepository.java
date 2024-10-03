package com.ctecx.brsuite.productcategory;


import java.util.List;
import java.util.Map;

public interface CustomCategoryRepository {


    List<Map<String, Object>> loadWaiterMenuCategories();
    List<Map<String, Object>> loadProductsByMenuCategories(int categoryID);
}
