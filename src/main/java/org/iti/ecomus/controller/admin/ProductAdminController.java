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
import jakarta.validation.constraints.Min;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private Validator validator;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<ProductDTO>> getProducts(@PagingAndSortingParam(
                                                                    model = AppConstants.PRODUCT_MODEL,
                                                                    defaultSortField = "productId"
                                                            ) PagingAndSortingHelper helper,
                                                                 @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                                                 @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        return ResponseEntity.ok(productService.getAllProducts(helper, pageNum, pageSize));
    }

    @GetMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping(path = "/name/{name}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDTO>> getProductByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.findByProductName(name));
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<ProductDTO> addProduct(@RequestPart("product") @Valid @JsonFormat String productJson, @RequestPart("images") MultipartFile[] images) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        NewProductDTO product = objectMapper.readValue(productJson, NewProductDTO.class);

        Set<ConstraintViolation<NewProductDTO>> violations = validator.validate(product);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        ProductDTO productId = productService.addProductWithCategories(product);
        productService.uploadProductImages(productId.getProductId(), images);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }

    @PostMapping(path = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadProductImages(
            @RequestPart("id") String productIdStr,
            @RequestPart("images") MultipartFile[] images) {
        Long productId;
        try {
            productId = Long.parseLong(productIdStr);
            if (productId < 1) {
                return ResponseEntity.badRequest().build();
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        if (images == null || images.length == 0) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println("Uploading images for product ID: " + productId);
        productService.uploadProductImages(productId, images);
        return ResponseEntity.noContent().build();
    }


@DeleteMapping(path = "/{id}/images/{imageName}")
    public ResponseEntity<Void> deleteProductImage(@PathVariable("id") @Min(1) Long id, @PathVariable("imageName") String imageName) {
        boolean deleted = productService.deleteProductImage(id, imageName);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{id}/images",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getProductImages(@PathVariable("id") @Min(1) Long id) {
        List<String> images = productService.getProductImages(id);
        return ResponseEntity.ok(images);
    }

    @PutMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("id") @Min(1) Long id, @RequestBody @Valid NewProductDTO product){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.updateProduct(id, product));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") @Min(1) Long id){
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }


}