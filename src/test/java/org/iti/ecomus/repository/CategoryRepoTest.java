package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryRepoTest {

    private CategoryRepo categoryRepo;

    @BeforeEach
    void setUp() {
        categoryRepo = mock(CategoryRepo.class);
    }

    @Test
    void testFindAllCategoryIdsByCategoryName() {
        // Arrange
        List<String> names = Arrays.asList("Electronics", "Books");
        List<Integer> expectedIds = Arrays.asList(1, 2);

        when(categoryRepo.findAllCategoryIdsByCategoryName(names)).thenReturn(expectedIds);

        // Act
        List<Integer> actualIds = categoryRepo.findAllCategoryIdsByCategoryName(names);

        // Assert
        assertEquals(expectedIds, actualIds);
        verify(categoryRepo).findAllCategoryIdsByCategoryName(names);
    }

    @Test
    void testGetCategoryByCategoryName() {
        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Books");

        when(categoryRepo.getCategoryByCategoryName("Books")).thenReturn(category);

        // Act
        Category result = categoryRepo.getCategoryByCategoryName("Books");

        // Assert
        assertNotNull(result);
        assertEquals("Books", result.getCategoryName());
        assertEquals(1, result.getCategoryId());
        verify(categoryRepo).getCategoryByCategoryName("Books");
    }
}
