package com.ctecx.brsuite.packproducts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/package-products")
public class PackageProductController {

    private final PackageProductService packageProductService;

    public PackageProductController(PackageProductService packageProductService) {
        this.packageProductService = packageProductService;
    }

    @PostMapping
    public ResponseEntity<PackageProduct> create(@RequestBody PackageProduct packageProduct) {
        return ResponseEntity.ok(packageProductService.save(packageProduct));
    }
}