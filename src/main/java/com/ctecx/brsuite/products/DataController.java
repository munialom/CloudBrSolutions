package com.ctecx.brsuite.products;

import com.ctecx.brsuite.brands.Brand;
import com.ctecx.brsuite.brands.BrandNotFoundException;
import com.ctecx.brsuite.brands.BrandService;
import com.ctecx.brsuite.productcategory.Category;
import com.ctecx.brsuite.productcategory.CategoryService;
import com.ctecx.brsuite.revenue.Revenue;
import com.ctecx.brsuite.revenue.RevenueService;
import com.ctecx.brsuite.uom.Uom;
import com.ctecx.brsuite.uom.UomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DataController {
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final RevenueService revenueCodeService;
    private final UomService productUnitService;

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping("/brands")
    public List<Brand> getAllBrands() {
        return brandService.getAllBrands();
    }

    @PostMapping("/brands")
    public Brand createBrand(@RequestBody Brand brand) {
        return brandService.createBrand(brand);
    }

    @GetMapping("/revenue-codes")
    public List<Revenue> getAllRevenueCodes() {
        return revenueCodeService.getAllRevenueCodes();
    }

    @PostMapping("/revenue-codes")
    public Revenue createRevenueCode(@RequestBody Revenue revenueCode) {
        return revenueCodeService.createRevenueCode(revenueCode);
    }

    @GetMapping("/product-units")
    public List<Uom> getAllProductUnits() {
        return productUnitService.getAllProductUnits();
    }

    @PostMapping("/product-units")
    public Uom createProductUnit(@RequestBody Uom productUnit) {
        return productUnitService.createProductUnit(productUnit);
    }
    @DeleteMapping("/brands")
    public ResponseEntity<Void> deleteAllBrands() {
        brandService.deleteAllBrands();
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("id") Long id) {
        try {
            brandService.deleteBrandById(id);
            return ResponseEntity.noContent().build();
        } catch (BrandNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}