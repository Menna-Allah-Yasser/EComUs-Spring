package org.iti.ecomus.repository;

import org.iti.ecomus.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepo extends JpaRepository<ProductCategory, Long> {

    int countProductsByCategory_CategoryId(Long categoryId);
}
