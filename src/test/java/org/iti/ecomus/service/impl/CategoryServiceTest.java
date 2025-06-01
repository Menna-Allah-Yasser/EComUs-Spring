package org.iti.ecomus.service.impl;

import org.iti.ecomus.dto.CategoryDTO;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.exceptions.*;
import org.iti.ecomus.mappers.CategoryMapper;
import org.iti.ecomus.repository.CategoryRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private CategoryMapper categoryMapper;

    private Category category;

    private String generateUniqueName(String base) {
        return base + "_" + UUID.randomUUID().toString().substring(0, 8);
    }

    @BeforeEach
    void setUp() {
        String name = generateUniqueName("Electronics");
        category = new Category();
        category.setCategoryName(name);
        category = categoryRepo.save(category);
    }

    // -----------------------------
    @Nested
    class GetAllCategoriesTests {

        @Test
        void testGetAllCategories() {
            List<CategoryDTO> categories = categoryService.getAllCategories();
            assertFalse(categories.isEmpty());
        }
    }

    // -----------------------------
    @Nested
    class GetCategoryByIdTests {

        @Test
        void testGetCategoryById_ValidId() {
            CategoryDTO dto = categoryService.getCategoryById(category.getCategoryId());
            assertNotNull(dto);
            assertEquals(category.getCategoryName(), dto.getCategoryName());
        }

        @Test
        void testGetCategoryById_NotValid() {
            assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryById(999999L));
        }

        @Test
        void testGetCategoryById_NullIdThrowsException() {
            assertThrows(BadRequestException.class, () -> categoryService.getCategoryById(null));
        }
    }

    // -----------------------------
    @Nested
    class AddCategoryTests {

        @Test
        void testAddCategory_Success() {
            CategoryDTO dto = new CategoryDTO();
            dto.setCategoryName(generateUniqueName("Books"));
            categoryService.addCategory(dto);

            assertTrue(categoryRepo.existsCategoriesByCategoryName(dto.getCategoryName()));
        }

        @Test
        void testAddCategory_NullDto_ThrowsException() {
            assertThrows(BadRequestException.class, () -> categoryService.addCategory(null));
        }

        @Test
        void testAddCategory_NullName_ThrowsException() {
            CategoryDTO dto = new CategoryDTO();
            dto.setCategoryName(null);
            assertThrows(BadRequestException.class, () -> categoryService.addCategory(dto));
        }

        @Test
        void testAddCategory_BlankName_ThrowsException() {
            CategoryDTO dto = new CategoryDTO();
            dto.setCategoryName("   ");
            assertThrows(BadRequestException.class, () -> categoryService.addCategory(dto));
        }

        @Test
        void testAddCategory_DuplicateNameThrowsException() {
            CategoryDTO dto = new CategoryDTO();
            dto.setCategoryName(category.getCategoryName());
            assertThrows(ConflictException.class, () -> categoryService.addCategory(dto));
        }
    }

    // -----------------------------
    @Nested
    class UpdateCategoryTests {

        @Test
        void testUpdateCategory_NullDto_ThrowsException() {
            assertThrows(BadRequestException.class, () -> categoryService.updateCategory(null));
        }

        @Test
        void testUpdateCategory_NullId_ThrowsException() {
            CategoryDTO dto = new CategoryDTO();
            dto.setCategoryId(null);
            dto.setCategoryName("ValidName");
            assertThrows(BadRequestException.class, () -> categoryService.updateCategory(dto));
        }

        @Test
        void testUpdateCategory_BlankName_ThrowsException() {
            CategoryDTO dto = new CategoryDTO();
            dto.setCategoryId(category.getCategoryId());
            dto.setCategoryName("   ");
            assertThrows(BadRequestException.class, () -> categoryService.updateCategory(dto));
        }

        @Test
        void testUpdateCategory_CategoryNotFound_ThrowsException() {
            CategoryDTO dto = new CategoryDTO();
            dto.setCategoryId(999999L);
            dto.setCategoryName("UpdatedName");
            assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(dto));
        }

        @Test
        void testUpdateCategory_DuplicateName_ThrowsException() {
            String duplicateName = generateUniqueName("Duplicate");
            Category other = new Category();
            other.setCategoryName(duplicateName);
            categoryRepo.save(other);

            CategoryDTO dto = categoryMapper.toCategoryDTO(category);
            dto.setCategoryName(duplicateName);
            assertThrows(ConflictException.class, () -> categoryService.updateCategory(dto));
        }

        @Test
        void testUpdateCategory_Success() {
            CategoryDTO dto = categoryMapper.toCategoryDTO(category);
            String updatedName = generateUniqueName("Updated");
            dto.setCategoryName(updatedName);

            categoryService.updateCategory(dto);

            Category updated = categoryRepo.findById(dto.getCategoryId()).orElse(null);
            assertNotNull(updated);
            assertEquals(updatedName, updated.getCategoryName());
        }

        @Test
        void testUpdateCategory_WithSameName_NoConflict() {
            CategoryDTO dto = categoryMapper.toCategoryDTO(category);
            assertDoesNotThrow(() -> categoryService.updateCategory(dto));
        }
    }

    // -----------------------------
    @Nested
    class DeleteCategoryTests {

        @Test
        void testDeleteCategory_Success() {
            categoryService.deleteCategory(category.getCategoryId());
            assertFalse(categoryRepo.existsById(category.getCategoryId()));
        }

        @Test
        void testDeleteCategory_NotExistThrows() {
            assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(999L));
        }
    }

    // -----------------------------
    @Nested
    class GetCategoryIdByNameTests {

        @Test
        void testGetCategoryIdByName_Valid() {
            Long id = categoryService.getCategoryIdByName(category.getCategoryName());
            assertEquals(category.getCategoryId(), id);
        }

        @Test
        void testGetCategoryIdByName_NotFoundThrows() {
            String randomName = generateUniqueName("NonExisting");
            assertThrows(ResourceNotFoundException.class, () -> categoryService.getCategoryIdByName(randomName));
        }
    }

}
