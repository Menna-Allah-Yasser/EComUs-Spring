package org.iti.ecomus.service.impl;

import org.iti.ecomus.dto.CategoryDTO;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.exceptions.*;
import org.iti.ecomus.mappers.CategoryMapper;
import org.iti.ecomus.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    CategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepo.findAll()
                .stream()
                .map(categoryMapper::toCategoryDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long categoryId) {
        if (categoryId == null) {
            throw new BadRequestException("Category ID cannot be null");
        }
        return categoryRepo.findById(categoryId)
                .map(categoryMapper::toCategoryDTO)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with ID: " + categoryId));
    }

    public CategoryDTO addCategory(CategoryDTO dto) {
        if (dto == null || dto.getCategoryName() == null || dto.getCategoryName().isBlank()) {
            throw new BadRequestException("Category name cannot be empty");
        }
        String trimmedName = dto.getCategoryName().trim();
        if (isCategoryNameExists(trimmedName)) {
            throw new ConflictException("Category name already exists");
        }
        Category category = categoryMapper.toCategory(dto);

        category.setCategoryId(null);
        category.setCategoryName(trimmedName);
        Category saved = categoryRepo.save(category);
        return categoryMapper.toCategoryDTO(saved);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        if (categoryId == null) {
            throw new BadRequestException("Category ID cannot be null");
        }

        boolean exists = categoryRepo.existsById(categoryId);
        if (!exists) {
            throw new ResourceNotFoundException("Category with ID " + categoryId + " not found");
        }

        categoryRepo.deleteById(categoryId);
    }


    public CategoryDTO updateCategory(CategoryDTO dto) {

        if (dto == null || dto.getCategoryId() == null) {
            throw new BadRequestException("Invalid data");
        }

        String trimmedName = dto.getCategoryName().trim();

        if (trimmedName.isEmpty()) {
            throw new BadRequestException("Category name cannot be empty");
        }

        Category existing = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        if (!existing.getCategoryName().equals(trimmedName)
                && isCategoryNameExists(trimmedName)) {
            throw new ConflictException("Category name already used");
        }

        Category updated = categoryMapper.toCategory(dto);
        updated.setCategoryId(existing.getCategoryId());
        updated.setCategoryName(trimmedName);
        Category updatedCategory = categoryRepo.save(updated);
        return categoryMapper.toCategoryDTO(updatedCategory);
    }


    private boolean isCategoryNameExists(String categoryName) {
        return categoryRepo.existsCategoriesByCategoryName(categoryName);
    }

    @Transactional(readOnly = true)
    public Long getCategoryIdByName(String categoryName) {
        Category category = categoryRepo.getCategoryByCategoryName(categoryName);
        if (category == null) {
            throw new ResourceNotFoundException("Category not found with name: " + categoryName);
        }
        return category.getCategoryId();
    }

}
