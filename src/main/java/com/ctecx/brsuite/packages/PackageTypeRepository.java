package com.ctecx.brsuite.packages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageTypeRepository extends JpaRepository<PackageType, Long> {
    List<PackageType> findByPackageNameContainingIgnoreCase(String packageName);
}