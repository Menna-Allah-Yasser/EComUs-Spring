package org.iti.ecomus.repository;


import org.iti.ecomus.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProductRepoTest {

    @Autowired
    private ProductRepo productRepo;

    @Test
    void testFindProductsByQuantityGreaterThanZero() {
        // Given: Adding test data to the in-memory database
        Product product1 = new Product("Product A", "Description A", 10, BigDecimal.valueOf(100));
        Product product2 = new Product("Product B", "Description B", 0, BigDecimal.valueOf(50));
        Product product3 = new Product("Product C", "Description C", 20, BigDecimal.valueOf(150));
        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);

        // When: Calling the repository method under test
        List<Product> products = productRepo.findProductsByQuantityGreaterThanZero();

        // Then: Verifying the results
        assertEquals(2, products.size(), "Should return 2 products with quantity > 0");
        assertTrue(products.stream().allMatch(product -> product.getQuantity() > 0), "All products should have quantity greater than zero.");
    }
}
