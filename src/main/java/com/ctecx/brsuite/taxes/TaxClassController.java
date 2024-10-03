package com.ctecx.brsuite.taxes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/taxclasses")
public class TaxClassController {
    private final TaxClassService taxClassService;

    @Autowired
    public TaxClassController(TaxClassService taxClassService) {
        this.taxClassService = taxClassService;
    }

    @GetMapping
    public List<TaxClass> getAllTaxClasses() {
        return taxClassService.getAllTaxClasses();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaxClass> getTaxClassById(@PathVariable Long id) {
        Optional<TaxClass> taxClass = taxClassService.getTaxClassById(id);
        return taxClass.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TaxClass> createTaxClass(@RequestBody TaxClass taxClass) {
        TaxClass createdTaxClass = taxClassService.createTaxClass(taxClass);
        return ResponseEntity.ok(createdTaxClass);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxClass> updateTaxClass(@PathVariable Long id, @RequestBody TaxClass taxClass) {
        TaxClass updatedTaxClass = taxClassService.updateTaxClass(id, taxClass);
        if (updatedTaxClass != null) {
            return ResponseEntity.ok(updatedTaxClass);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTaxClass(@PathVariable Long id) {
        taxClassService.deleteTaxClass(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/calculateTax")
    public double calculateTax(@RequestBody TaxRequest taxRequest) {

        return taxClassService.calculateInclusiveTax(taxRequest.getAmount(), taxRequest.getTaxIds());
    }

}
