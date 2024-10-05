package com.ctecx.brsuite.products;


import java.util.List;
import java.util.Map;

public interface CustomProductRepository {


    List<Map<String, Object>> getAllProducts();

    List<Map<String, Object>> searchProductsWithPositiveStock(String searchKey, int pageSize, int offset);

    List<Map<String, Object>> GetBelowLowStockLevels();
    List<Map<String, Object>> GetLowStockLevels ();

    List<Map<String, Object>> search_products_by_code(String productCode);
}
