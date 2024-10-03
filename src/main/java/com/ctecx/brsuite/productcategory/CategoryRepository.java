package com.ctecx.brsuite.productcategory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>,CustomCategoryRepository {
    @Query("SELECT s FROM Category s where s.parent.id=NULL")
    List<Category> listRootStrands();

    List<Category> findByParentIsNull();

    Category findByName(String name);
    List<Category> findAllByOrderByIdAsc();


    List<Category> findByNameNot(String debtors);
}
