package com.ctecx.brsuite.productcategory;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {


    private final CategoryRepository accountChartRepository;


    public void saveStrands(Category accountChart) {

        accountChartRepository.save(accountChart);
    }

    public List<Map<String, Object>> productByCategories( int categoryID) {
        return accountChartRepository.loadProductsByMenuCategories(categoryID);
    }

    public List<Map<String, Object>> getKeyMenuCategories() {
        return accountChartRepository.loadWaiterMenuCategories();
    }


    public void deleletAccountChartById(Integer id) {
        accountChartRepository.deleteById(id);
    }


    public List<Category> getTopLevelAccounts() {
        return accountChartRepository.findByParentIsNull();
    }

    public Optional<Category> getAccountChartByIdLevel(Integer id) {
        return accountChartRepository.findById(id);
    }

    public Optional<Category> getCategoryById(Integer id) {
        return accountChartRepository.findById(id);
    }


    public List<Category> getAllCategories() {

        return  accountChartRepository.findAll();
    }

    public Category createCategory(Category category) {
        Category parentCategory = null;
        if (category.getParent() != null && category.getParent().getId() != null) {
            parentCategory = accountChartRepository.findById(category.getParent().getId()).orElse(null);
        }
        category.setParent(parentCategory);
        return accountChartRepository.save(category);
    }
}
