package com.ctecx.brsuite.suppliers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;


    public List<Supplier> searchSuppliers(String keyword) {
        List<Supplier> suppliers = supplierRepository.findByCreditorNameContainingIgnoreCase(keyword);
        return suppliers.stream()
                .map(supplier -> new Supplier(
                        supplier.getId(),
                        supplier.getCreditorName(),
                        supplier.getCreditorAddress(),
                        supplier.getCreditorContact(),
                        supplier.getCreditTerms(),
                        supplier.getTaxPin(),
                        supplier.getCreditlimit(),
                        supplier.getEmail(),
                        supplier.getCustomerType()
                ))
                .collect(Collectors.toList());
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier saveSupplier(Supplier supplier) {
        return supplierRepository.save(supplier);
    }

    public Optional<Supplier> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}