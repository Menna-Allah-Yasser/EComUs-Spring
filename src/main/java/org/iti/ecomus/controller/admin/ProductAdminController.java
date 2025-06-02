package org.iti.ecomus.controller.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.NewProductDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.mappers.NewProductMapper;
import org.iti.ecomus.mappers.ProductMapper;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.iti.ecomus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api/admin/products")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Admin - Products", description = "Admin product management")
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Validator validator;


    @GetMapping
    public ResponseEntity<PagedResponse<ProductDTO>> getProducts(@PagingAndSortingParam(
                                                                    model = AppConstants.PRODUCT_MODEL,
                                                                    defaultSortField = "productId"
                                                            ) PagingAndSortingHelper helper,
                                                                 @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                                                 @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        return ResponseEntity.ok(productService.getAllProducts(helper, pageNum, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductDTO>> getProductByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.findByProductName(name));
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Long> addProduct(@RequestPart("product") @Valid @JsonFormat String productJson, @RequestPart("images") MultipartFile[] images) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        NewProductDTO product = objectMapper.readValue(productJson, NewProductDTO.class);

        Set<ConstraintViolation<NewProductDTO>> violations = validator.validate(product);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        Long productId = productService.addProductWithCategories(product);
        productService.uploadProductImages(productId, images);
        return ResponseEntity.ok(productId);
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<String>> getProductImages(@PathVariable("id") Long id) {
        List<String> images = productService.getProductImages(id);
        return ResponseEntity.ok(images);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") Long id, @RequestBody @Valid NewProductDTO product){
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProductById(id);
        return ResponseEntity.ok("Product deleted successfully");
    }


}