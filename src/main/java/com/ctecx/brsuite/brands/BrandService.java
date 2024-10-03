package com.ctecx.brsuite.brands;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BrandService {
    private final BrandRepository brandRepository;

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Optional<Brand> getBrandById(Long id) {
        return brandRepository.findById(id);
    }

    public Brand createBrand(Brand brand) {
        return brandRepository.save(brand);
    }

    @Transactional
    public Brand updateBrand(Long id, Brand updatedBrand) {
        return brandRepository.findById(id)
                .map(existingBrand -> {
                    existingBrand.setName(updatedBrand.getName());
                    // Update other fields as needed
                    return brandRepository.save(existingBrand);
                })
                .orElseThrow(() -> new BrandNotFoundException("Brand not found with id: " + id));
    }

    public void deleteBrandById(Long id) {
        if (!brandRepository.existsById(id)) {
            throw new BrandNotFoundException("Brand not found with id: " + id);
        }
        brandRepository.deleteById(id);
    }

    @Transactional
    public void deleteAllBrands() {
        brandRepository.deleteAll();
    }
}