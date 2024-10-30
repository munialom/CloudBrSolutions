package com.ctecx.brsuite.products;

import com.ctecx.brsuite.stockmode.StockService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api-bulk")
@AllArgsConstructor
public class ProductRestController {

    private final ProductService productService;
    private final CustomProductService customProductService;
    private final StockService stockService;


    @GetMapping("/SearchByCode/{productCode}")
    public ResponseEntity<ProductSaleDTO> getProductByCode(@PathVariable String productCode) {
        return customProductService.searchProductByCode(productCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/SearchByCode_All/{productCode}")
    public ResponseEntity<ProductSaleDTO> getProductByCodeAll(@PathVariable String productCode) {
        return customProductService.searchProductByCode_all(productCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/products")
    public ResponseEntity<Product> createProduct(@RequestBody ProductDTO productDTO) {
        Product savedProduct = productService.saveProduct(productDTO);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @GetMapping("/generate-code")
    public ResponseEntity<String> generateProductCode() {
        String productCode = ProductCodeGenerator.generateProductCode();
        return ResponseEntity.ok(productCode);
    }

    @GetMapping(path = "/products-database", produces = "application/json")
    public ResponseEntity<List<Map<String, Object>>> getAllProductsData() {
        List<Map<String, Object>> members = customProductService.getAllProducts();
        return ResponseEntity.ok(members);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTOList>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProductsData());
    }

    @PostMapping("upload")
    public ResponseEntity<String> uploadProducts(@RequestParam("file") MultipartFile file) {
        try {
            productService.uploadProducts(file);
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Products uploaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while uploading products: " + e.getMessage());
        }
    }

    @PostMapping("upload-qty")
    public ResponseEntity<String> uploadQtyProducts(@RequestParam("file") MultipartFile file) {
        stockService.uploadProductsFromExcel(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Products uploaded successfully!");
    }

    @GetMapping("/below-low-stock")
    public ResponseEntity<List<Map<String, Object>>> getBelowLowStockLevels() {
        List<Map<String, Object>> belowLowStockProducts = customProductService.GetBelowLowStockLevels();
        return ResponseEntity.ok(belowLowStockProducts);
    }


    @GetMapping("/cooking-gas-stock")
    public ResponseEntity<List<Map<String, Object>>> CookingGasStockLevels() {
        List<Map<String, Object>> belowLowStockProducts = customProductService.GetCookingGasStockLevels();
        return ResponseEntity.ok(belowLowStockProducts);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Map<String, Object>>> getLowStockLevels() {
        List<Map<String, Object>> lowStockProducts = customProductService.GetLowStockLevels();
        return ResponseEntity.ok(lowStockProducts);
    }
}