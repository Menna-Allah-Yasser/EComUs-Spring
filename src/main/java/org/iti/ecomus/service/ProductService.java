package org.iti.ecomus.service;

import org.iti.ecomus.dto.NewProductDTO;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Product;

import java.util.List;
import java.util.Optional;

//@Repository
public interface ProductService {



    ProductDTO getProductById(Long productId);

    List<ProductDTO> getAllProducts() ;

    void deleteProductById(Long productId);

    Optional<List<ProductDTO>> getProductsByQuantityGreaterThan(int quantity);

    ProductDTO updateProduct(Long id, Product product);

    List<ProductDTO> findByProductName(String name);

    Long addProductWithCategories(NewProductDTO newProductDTO);
}