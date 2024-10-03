package com.ctecx.brsuite.packages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Controller
@RestController
@RequestMapping("/api/packageTypes")
public class PackageTypeController {

    private final PackageTypeService packageTypeService;

    @Autowired
    public PackageTypeController(PackageTypeService packageTypeService) {
        this.packageTypeService = packageTypeService;
    }

    @GetMapping(path = "/search", produces = "application/json")
    public List<PackageType> findByPackageName(@RequestParam String keyword) {
        return packageTypeService.findByPackageName(keyword);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody PackageType packageType) {
        Map<String, Object> response = new HashMap<>();
        try {
            PackageType savedPackageType = packageTypeService.save(packageType);
            response.put("success", true);
            response.put("packageType", savedPackageType);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to save package type data.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    @GetMapping
    public List<PackageType> findAll() {
        return packageTypeService.findAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        packageTypeService.deleteById(id);
    }
}
