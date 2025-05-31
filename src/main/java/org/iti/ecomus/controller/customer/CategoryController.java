package org.iti.ecomus.controller.customer;

import jakarta.annotation.security.RolesAllowed;
import org.iti.ecomus.dto.*;
import org.iti.ecomus.service.impl.*;
import org.iti.ecomus.util.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PostMapping
    @RolesAllowed("ADMIN")
    public ResponseEntity<CategoryDTO> createCategory(@Validated(OnAdd.class) @RequestBody CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.addCategory(categoryDTO));
    }

    @PutMapping()
    @RolesAllowed("ADMIN")
    public ResponseEntity<CategoryDTO> updateCategory(@RequestBody @Validated(OnUpdate.class) CategoryDTO categoryDTO) {
        return ResponseEntity.ok(categoryService.updateCategory(categoryDTO));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("ADMIN")
    public void deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
    }

}
