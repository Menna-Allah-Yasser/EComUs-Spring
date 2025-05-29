package org.iti.ecomus.mapper;

import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.dto.CategoryDTO;
import org.iti.ecomus.dto.CategoryNoProductDTO;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.mappers.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class CategoryMapperTest {

    @Autowired
    private CategoryMapper categoryMapper;


    @Test
    void testToCategory() {
        // Given
        String categoryName = "ELECTRONICS";
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(1L);
        productDTO.setProductName("Smartphone");
        productDTO.setDescription("Latest model smartphone");
        productDTO.setPrice(new BigDecimal("699.99"));
        productDTO.setCategories(Arrays.asList(new CategoryNoProductDTO(1L, categoryName)));
        CategoryDTO category = new CategoryDTO(1L,categoryName, Arrays.asList(productDTO));

        // When


        Category categoryEntity = categoryMapper.toCategory(category);

        // Then
        assertNotNull(categoryEntity);
        assertEquals(1L, categoryEntity.getCategoryId());
        assertEquals(categoryName, categoryEntity.getCategoryName());
        assertNotNull(categoryEntity.getProducts());
        assertEquals(1, categoryEntity.getProducts().size());
        Product product = categoryEntity.getProducts().get(0);
        assertEquals(1L, product.getProductId());
        assertEquals("Smartphone", product.getProductName());
        assertEquals("Latest model smartphone", product.getDescription());
        assertEquals(new BigDecimal("699.99"), product.getPrice());
        assertNotNull(product.getCategories());
        assertEquals(1, product.getCategories().size());
        assertEquals(1L, product.getCategories().get(0).getCategoryId());
        assertEquals(categoryName, product.getCategories().get(0).getCategoryName());

    }

    @Test
    void testToCategoryDTO() {
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

        category.setProducts(Arrays.asList(product));

        // When
        CategoryDTO categoryDTO = categoryMapper.toCategoryDTO(category);

        // Then
        assertNotNull(categoryDTO);
        assertEquals(1L, categoryDTO.getCategoryId());
        assertEquals("ELECTRONICS", categoryDTO.getCategoryName());
        assertNotNull(categoryDTO.getProducts());
        assertEquals(1, categoryDTO.getProducts().size());
        ProductDTO productDTO = categoryDTO.getProducts().get(0);
        assertEquals(1L, productDTO.getProductId());
        assertEquals("Smartphone", productDTO.getProductName());
        assertEquals("Latest model smartphone", productDTO.getDescription());
        assertEquals(new BigDecimal("699.99"), productDTO.getPrice());
        assertNotNull(productDTO.getCategories());
        assertEquals(1, productDTO.getCategories().size());
        assertEquals(1L, productDTO.getCategories().get(0).getCategoryId());
        assertEquals("ELECTRONICS", productDTO.getCategories().get(0).getCategoryName());
    }

    @Test
    void testToCategoryDTOList() {
        // Given
        Category category1 = new Category();
        category1.setCategoryId(1L);
        category1.setCategoryName("ELECTRONICS");

        Category category2 = new Category();
        category2.setCategoryId(2L);
        category2.setCategoryName("BOOKS");

        List<Category> categories = Arrays.asList(category1, category2);

        // When
        List<CategoryDTO> dtos = categoryMapper.toDtoList(categories);

        // Then
        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        CategoryDTO dto1 = dtos.get(0);
        assertEquals(1L, dto1.getCategoryId());
        assertEquals("ELECTRONICS", dto1.getCategoryName());

        CategoryDTO dto2 = dtos.get(1);
        assertEquals(2L, dto2.getCategoryId());
        assertEquals("BOOKS", dto2.getCategoryName());
    }

    @Test
    void testToCategoryEntityList() {
        // Given
        CategoryDTO dto1 = new CategoryDTO();
        dto1.setCategoryId(10L);
        dto1.setCategoryName("TOYS");

        CategoryDTO dto2 = new CategoryDTO();
        dto2.setCategoryId(20L);
        dto2.setCategoryName("FURNITURE");

        List<CategoryDTO> dtos = Arrays.asList(dto1, dto2);

        // When
        List<Category> entities = categoryMapper.toCategoryList(dtos);

        // Then
        assertNotNull(entities);
        assertEquals(2, entities.size());

        Category entity1 = entities.get(0);
        assertEquals(10L, entity1.getCategoryId());
        assertEquals("TOYS", entity1.getCategoryName());

        Category entity2 = entities.get(1);
        assertEquals(20L, entity2.getCategoryId());
        assertEquals("FURNITURE", entity2.getCategoryName());
    }

}
