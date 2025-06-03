package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.paging.SearchRepository;
import org.iti.ecomus.specification.ProductSpecification;
import org.iti.ecomus.specification.UserSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface ProductRepo extends JpaRepository<Product, Long>, SearchRepository<Product,Long> {

    List<Product> findByQuantityGreaterThanEqual(int quantity);


    List<Product> findByProductName(String name);

    List<Product> findByQuantityGreaterThan(int quantity);

    Boolean existsByProductId(Long productId);



    @Override
    default public Specification<Product> getFiltersSpecification(String keyword, Map<String, Object> searchParams) {
        return ProductSpecification.build(keyword, searchParams);
    }
}
