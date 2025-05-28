package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {

    List<Product> findByQuantityGreaterThan(int quantity);
}
