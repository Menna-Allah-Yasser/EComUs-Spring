package org.iti.ecomus.controller.customer;

import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/public/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<ProductDTO>> getProductByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(productService.findByProductName(name));
    }

    @GetMapping("/quantity/{quantity}")
    public ResponseEntity<Optional<List<ProductDTO>>> getProductByQuantity(@PathVariable("quantity") int quantity){
        return ResponseEntity.ok(productService.getProductsByQuantityGreaterThan(quantity));
    }




}
