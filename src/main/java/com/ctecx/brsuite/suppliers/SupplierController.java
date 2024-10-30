package com.ctecx.brsuite.suppliers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;


    @GetMapping(path = "/search", produces = "application/json")
    public List<Vendor> searchSuppliers(@RequestParam String keyword) {
        return supplierService.searchSuppliers(keyword);
    }

    @GetMapping
    public ResponseEntity<List<Vendor>> getAllSuppliers() {
        return ResponseEntity.ok(supplierService.getAllSuppliers());
    }

    @PostMapping
    public ResponseEntity<Vendor> createSupplier(@RequestBody Vendor vendor) {
        return ResponseEntity.ok(supplierService.saveSupplier(vendor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getSupplierById(@PathVariable Long id) {
        return supplierService.getSupplierById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}