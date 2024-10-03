package com.ctecx.brsuite.productcategory;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/waiter-menu-categories")
@RequiredArgsConstructor
public class CategoryRestController {

private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllMenuCategories() {
        List<Map<String, Object>> result =categoryService.getKeyMenuCategories() ;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @GetMapping("/products-by-category")
    public ResponseEntity<List<Map<String, Object>>> getProductsByCategory(@RequestParam("categoryId") int categoryId) {
        try {
            List<Map<String, Object>> products =categoryService.productByCategories(categoryId);

            if (products.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(products);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }


}
