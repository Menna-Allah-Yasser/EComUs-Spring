package org.iti.ecomus.service.impl;

import org.iti.ecomus.entity.ProductCategory;
import org.iti.ecomus.repository.ProductCategoryRepo;
import org.iti.ecomus.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    public int countProductsByCategory_CategoryId(Long categoryId){
        return productCategoryRepo.countProductsByCategory_CategoryId(categoryId);
    }

    public Optional<ProductCategory> addProductCategory(ProductCategory productCategory){
        return Optional.ofNullable(productCategoryRepo.save(productCategory));
    }

}
