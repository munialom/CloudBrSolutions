package com.ctecx.brsuite.products;

import com.ctecx.brsuite.stockmode.StockService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api-bulk")
@AllArgsConstructor
public class ProductRestController {

 private final ProductService productService;
 private final CustomProductService customProductService;

 private final StockService stockService;


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


    @GetMapping("/products/value")
    public ResponseEntity<List<ProductValueDTO>> getProductValues() {
        List<ProductValueDTO> productValues = productService.stockValue();
        return ResponseEntity.ok(productValues);
    }
    @GetMapping("invoice")
    public List<Invoice> getAllInvoices() {
        return Arrays.asList(
                new Invoice("1", "Item1", "Description1", "100", "10", "5", "105"),
                new Invoice("2", "Item2", "Description2", "200", "20", "10", "220"),
                new Invoice("2", "Item2", "Description2", "200", "20", "10", "220")

        );
    }
    @GetMapping
    public ResponseEntity<List<ProductDTOList>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProductsData());
    }




    @PostMapping("upload")
    public ResponseEntity<String> uploadProducts(@RequestParam("file") MultipartFile file) {
        try {
            // Call the service method to handle the upload
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


    @GetMapping("/sales")
    public ResponseEntity<List<ProductSaleDTO>> searchProductsForSales(@RequestParam(value = "searchKey", required = false) String searchKey, @PageableDefault(size = 5) Pageable pageable) {
        List<ProductSaleDTO> products = new ArrayList<>();
        if (searchKey != null && !searchKey.isEmpty()) {
            products = productService.salesByProductNameAndCode(searchKey, pageable.getPageNumber(), pageable.getPageSize());
        }
        return ResponseEntity.ok(products);
    }


}
