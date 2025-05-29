package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CategoryRepoTest {

    @Autowired
    private CategoryRepo categoryRepo;

    @BeforeEach
    void setUp() {
        Category cat1 = new Category();
        cat1.setCategoryName("Electronics1");

        Category cat2 = new Category();
        cat2.setCategoryName("Books1");

        categoryRepo.save(cat1);
        categoryRepo.save(cat2);
    }

    @Test
    void testFindByName_ShouldReturnCorrectCategory() {
        Category found = categoryRepo.getCategoryByCategoryName("Electronics1");
        assertNotNull(found);
        assertEquals("Electronics1", found.getCategoryName());
    }

    @Test
    void testFindAllCategoryIdsByCategoryName() {
        // Arrange
        Category cat1 = new Category();
        cat1.setCategoryName("Clothing1");


        Category cat2 = new Category();
        cat2.setCategoryName("Home1");

        categoryRepo.save(cat1);
        categoryRepo.save(cat2);

        // Act
        List<Long> ids = categoryRepo.findAllCategoryIdsByCategoryName(List.of("Clothing1", "Home1"));

        // Assert
        assertEquals(2, ids.size());
        assertTrue(ids.contains(cat1.getCategoryId()));
        assertTrue(ids.contains(cat2.getCategoryId()));
    }
}
