package com.ctecx.brsuite.packproducts;

import org.springframework.stereotype.Service;

@Service
public class PackageProductService {

    private final PackageProductRepository packageProductRepository;

    public PackageProductService(PackageProductRepository packageProductRepository) {
        this.packageProductRepository = packageProductRepository;
    }

    public PackageProduct save(PackageProduct packageProduct) {
        return packageProductRepository.save(packageProduct);
    }
}