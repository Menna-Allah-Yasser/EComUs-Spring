package org.iti.ecomus.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/products")
@SecurityRequirement(name = "E-Commerce Application")
@Tag(name = "Admin - Products", description = "Admin product management")
public class ProductAdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private NewProductMapper newProductMapper;

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

    @PostMapping()
    public ResponseEntity<Long> addProduct(@RequestBody @Valid NewProductDTO product) {
        Long productId = productService.addProductWithCategories(product);
        return ResponseEntity.ok(productId);
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