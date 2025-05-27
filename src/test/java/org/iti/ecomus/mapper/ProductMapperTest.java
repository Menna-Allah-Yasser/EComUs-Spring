package org.iti.ecomus.mapper;

import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.dto.CategoryNoProductDTO;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.mappers.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class ProductMapperTest {

    @Autowired
    private ProductMapper productMapper;

    @Test
    void testToProductDTO(){
        // Given
        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("ELECTRONICS");

        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Smartphone");
        product.setDescription("Latest model smartphone");
        product.setPrice(new BigDecimal("699.99"));
        product.setCategories(Arrays.asList(category));

        // When
        ProductDTO productDTO = productMapper.toProductDTO(product);

        // Then

        assertNotNull(productDTO);
        assertEquals("Smartphone", productDTO.getProductName());
        assertEquals(new BigDecimal("699.99"), productDTO.getPrice());
        assertEquals("Latest model smartphone", productDTO.getDescription());
        assertEquals(1L, productDTO.getProductId());
        assertEquals(1, productDTO.getCategories().size());
        assertEquals("ELECTRONICS", productDTO.getCategories().get(0).getCategoryName());

    }

    @Test
    void testToProductEntity(){
        // Given
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1L);
        productDTO.setProductName("Smartphone");
        productDTO.setDescription("Latest model smartphone");
        productDTO.setPrice(new BigDecimal("699.99"));
        CategoryNoProductDTO category = new CategoryNoProductDTO();
        category.setCategoryId(1L);
        category.setCategoryName("ELECTRONICS");
        productDTO.setCategories(Arrays.asList(category));

        // When
        Product product = productMapper.toProduct(productDTO);

        // Then
        assertNotNull(product);
        assertEquals("Smartphone", product.getProductName());
        assertEquals(new BigDecimal("699.99"), product.getPrice());
        assertEquals("Latest model smartphone", product.getDescription());
        assertEquals(1L, product.getProductId());
        assertEquals(1, product.getCategories().size());
        assertEquals("ELECTRONICS", product.getCategories().get(0).getCategoryName());
    }
}
