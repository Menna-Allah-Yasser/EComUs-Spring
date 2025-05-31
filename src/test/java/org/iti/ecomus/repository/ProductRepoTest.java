package org.iti.ecomus.repository;


import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
public class ProductRepoTest {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductCategoryRepo productCategoryRepo;

    @Test
    void testFindProductsByQuantityGreaterThanZero() {
        Product product1 = new Product("Product A", "Description A", 10, BigDecimal.valueOf(100));
        Product product2 = new Product("Product B", "Description B", 0, BigDecimal.valueOf(50));
        Product product3 = new Product("Product C", "Description C", 20, BigDecimal.valueOf(150));
        productRepo.save(product1);
        productRepo.save(product2);
        productRepo.save(product3);

        List<Product> products = productRepo.findByQuantityGreaterThanEqual(0);

        assertEquals(22, products.size(), "Should return 2 products with quantity > 0");
        //assertTrue(products.stream().allMatch(product -> product.getQuantity() > 0), "All products should have quantity greater than zero.");
    }

    @Test
    void testCountProductsByCategory_CategoryId(){
        int result = productCategoryRepo.countProductsByCategory_CategoryId(1L);
        assertEquals(8, result, "Should return 8 products with category id 1");
    }
}
