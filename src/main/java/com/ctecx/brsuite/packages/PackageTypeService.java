package com.ctecx.brsuite.packages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackageTypeService {

    private final PackageTypeRepository packageTypeRepository;

    @Autowired
    public PackageTypeService(PackageTypeRepository packageTypeRepository) {
        this.packageTypeRepository = packageTypeRepository;
    }

    public PackageType save(PackageType packageType) {
        packageType.setActive(true);
        return packageTypeRepository.save(packageType);
    }
    public List<PackageType> findByPackageName(String packageName) {
        return packageTypeRepository.findByPackageNameContainingIgnoreCase(packageName);
    }
    public Optional<PackageType> findById(Long id) {
        return packageTypeRepository.findById(id);
    }

    public List<PackageType> findAll() {
        return packageTypeRepository.findAll();
    }

    public void deleteById(Long id) {
        packageTypeRepository.deleteById(id);
    }
}
