package org.iti.ecomus.controller.customer;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.constraints.Min;
import org.iti.ecomus.dto.*;
import org.iti.ecomus.service.impl.*;
import org.iti.ecomus.util.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/category")
@Tag(name = "Customer - Categories", description = "Customer category browsing")
@Validated
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CategoryDTO>> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable("id") @Min(1) Long id) {
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

}
