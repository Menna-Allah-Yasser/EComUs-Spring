package org.iti.ecomus.service.impl;

import org.iti.ecomus.entity.Product;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    public Optional<Long> addProduct(Product product){
        return Optional.ofNullable(productRepo.save(product).getProductId());

    }

    public Optional<Product> getProductById(Long productId){
        return Optional.ofNullable(productRepo.findById(productId).orElse(null));
    }

    public Optional<List<Product>> getAllProducts() {

        return Optional.ofNullable(productRepo.findAll());
    }

    public void deleteProductById(Long productId){
        productRepo.deleteById(productId);
    }

    public Optional<List<Product>> getProductsByQuantityGreaterThan(int quantity){
        return Optional.ofNullable(productRepo.findByQuantityGreaterThan(quantity));
    }

    public Product updateProduct(Product product){
        return productRepo.save(product);
    }





}
