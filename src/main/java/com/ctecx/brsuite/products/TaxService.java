package com.ctecx.brsuite.products;

import com.ctecx.brsuite.taxes.TaxClass;

import java.util.ArrayList;
import java.util.List;

public class TaxService {

    public double calculateTax(Product product, String taxMode) {
        double totalTax = 0.0;

        // Check if tax mode is inclusive or exclusive
        if (taxMode.equalsIgnoreCase("inclusive")) {
            totalTax = calculateInclusiveTax(product);
        } else if (taxMode.equalsIgnoreCase("exclusive")) {
            totalTax = calculateExclusiveTax(product);
        }

        return totalTax;
    }

    private double calculateInclusiveTax(Product product) {
        double totalTax = 0.0;

        // Iterate through assigned tax classes
        for (TaxClass taxClass : product.getTaxClasses()) {
            totalTax += taxClass.getRate() * product.getProductPrice() / (1 + taxClass.getRate());
        }

        return totalTax;
    }

    private double calculateExclusiveTax(Product product) {
        double totalTax = 0.0;

        // Iterate through assigned tax classes
        for (TaxClass taxClass : product.getTaxClasses()) {
            totalTax += taxClass.getRate() * product.getProductPrice();
        }

        return totalTax;
    }




    public double calculateInclusiveTax(double amount, List<Long> taxIds) {
        // Fetch tax classes based on ids
        List<TaxClass> taxClasses = getTaxClassesFromIds(taxIds);

        double totalTax = 0.0;

        // Iterate through fetched tax classes
        for (TaxClass taxClass : taxClasses) {
            totalTax += taxClass.getRate() * amount / (1 + taxClass.getRate());
        }

        return totalTax;
    }

    private List<TaxClass> getTaxClassesFromIds(List<Long> taxIds) {
        // Convert tax ids to TaxClass objects
        // This is just a placeholder and will not compile
        List<TaxClass> taxClasses = new ArrayList<>();
        for (Long id : taxIds) {
            TaxClass taxClass = new TaxClass();
            taxClass.setId(id);
            // Set other properties of taxClass as needed
            taxClasses.add(taxClass);
        }

        return taxClasses;
    }

}
