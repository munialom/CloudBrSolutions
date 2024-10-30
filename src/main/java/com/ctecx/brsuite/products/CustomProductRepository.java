package com.ctecx.brsuite.products;


import java.util.List;
import java.util.Map;

public interface CustomProductRepository {


    List<Map<String, Object>> getAllProducts();



    List<Map<String, Object>> GetBelowLowStockLevels();
    List<Map<String, Object>> GetLowStockLevels ();

    Map<String, Object> search_product_by_code(String productCode);
    List<Map<String, Object>> searchProductsWithPositiveStock(String searchKey, int pageSize, int offset);



    Map<String, Object> search_product_by_code_all(String productCode);
    List<Map<String, Object>> searchProductsWithPositiveStock_all(String searchKey, int pageSize, int offset);

    List<Map<String, Object>> GetCookingGasStockLevels();


}
