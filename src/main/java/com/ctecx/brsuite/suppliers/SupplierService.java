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


    public List<Vendor> searchSuppliers(String keyword) {
        List<Vendor> vendors = supplierRepository.findByCreditorNameContainingIgnoreCase(keyword);
        return vendors.stream()
                .map(supplier -> new Vendor(
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

    public List<Vendor> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Vendor saveSupplier(Vendor vendor) {
        return supplierRepository.save(vendor);
    }

    public Optional<Vendor> getSupplierById(Long id) {
        return supplierRepository.findById(id);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}