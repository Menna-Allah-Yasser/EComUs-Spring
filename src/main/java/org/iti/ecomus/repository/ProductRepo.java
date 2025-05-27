package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {
}
