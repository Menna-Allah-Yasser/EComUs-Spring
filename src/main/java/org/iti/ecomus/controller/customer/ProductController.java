package org.iti.ecomus.controller.customer;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.iti.ecomus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/products")
@Tag(name = "Customer - Products", description = "Customer product browsing")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

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
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") @Min(1) Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }


    @GetMapping(path = "/{id}/images",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getProductImages(@PathVariable("id") Long id) {
        List<String> images = productService.getProductImages(id);
        return ResponseEntity.ok(images);
    }

    @GetMapping(path = "/name/{name}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDTO>> getProductByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.findByProductName(name));
    }

    @GetMapping(path = "/quantity/{quantity}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Optional<List<ProductDTO>>> getProductByQuantity(@PathVariable("quantity") int quantity){
        return ResponseEntity.ok(productService.getProductsByQuantityGreaterThan(quantity));
    }




}
