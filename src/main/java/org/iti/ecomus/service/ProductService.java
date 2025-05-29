package org.iti.ecomus.service;

import org.iti.ecomus.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//@Repository
public interface ProductService {


    Optional<Long> addProduct(Product product);

    Optional<Product> getProductById(Long productId);

    Optional<List<Product>> getAllProducts() ;

    void deleteProductById(Long productId);

    Optional<List<Product>> getProductsByQuantityGreaterThan(int quantity);

    Product updateProduct(Product product);
}
