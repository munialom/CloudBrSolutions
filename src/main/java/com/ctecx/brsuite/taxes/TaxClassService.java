package com.ctecx.brsuite.taxes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaxClassService {
    private final TaxClassRepository taxClassRepository;

    @Autowired
    public TaxClassService(TaxClassRepository taxClassRepository) {
        this.taxClassRepository = taxClassRepository;
    }

    public List<TaxClass> getAllTaxClasses() {
        return taxClassRepository.findAll();
    }

    public Optional<TaxClass> getTaxClassById(Long id) {
        return taxClassRepository.findById(id);
    }

    public TaxClass createTaxClass(TaxClass taxClass) {
        return taxClassRepository.save(taxClass);
    }

    public TaxClass updateTaxClass(Long id, TaxClass taxClass) {
        if (taxClassRepository.existsById(id)) {
            taxClass.setId(id);
            return taxClassRepository.save(taxClass);
        }
        return null; // Handle appropriately if the ID doesn't exist
    }

    public void deleteTaxClass(Long id) {
        taxClassRepository.deleteById(id);
    }




    public double calculateInclusiveTax(double amount, List<Long> taxIds) {
        // Fetch tax classes based on ids
        List<TaxClass> taxClasses = getTaxClassesFromIds(taxIds);

        double totalTax = 0.0;

        // Iterate through fetched tax classes
        for (TaxClass taxClass : taxClasses) {
            double rate = taxClass.getRate();
            double vatAmount = (rate / (100 + rate)) * amount;
            totalTax += vatAmount;
        }

        // Round off to 2 decimal places
        totalTax = Math.round(totalTax * 100.0) / 100.0;

        return totalTax;
    }

    private List<TaxClass> getTaxClassesFromIds(List<Long> taxIds) {
        // Fetch tax classes based on ids from the repository
        List<TaxClass> taxClasses = taxClassRepository.findAllById(taxIds);
        return taxClasses;
    }



}
