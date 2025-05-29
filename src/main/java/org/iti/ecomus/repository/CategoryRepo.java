package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Category;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface CategoryRepo extends JpaRepository<Category, Long> {
//
    @Query("SELECT c.categoryId FROM Category c WHERE c.categoryName IN :names")
    List<Long> findAllCategoryIdsByCategoryName(@Param("names") List<String> names);

    Category getCategoryByCategoryName(String categoryName);

    boolean existsCategoriesByCategoryName(String categoryName);

}
