package org.iti.ecomus.service;

import org.iti.ecomus.dto.NewProductDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

//@Repository
public interface ProductService {



    ProductDTO getProductById(Long productId);


    PagedResponse<ProductDTO> getAllProducts(PagingAndSortingHelper helper, int pageNum, int pageSize);

    void deleteProductById(Long productId);

    Optional<List<ProductDTO>> getProductsByQuantityGreaterThan(int quantity);


    ProductDTO updateProduct(Long id, NewProductDTO product);

    List<ProductDTO> findByProductName(String name);

    ProductDTO addProductWithCategories(NewProductDTO newProductDTO);

    void uploadProductImages(Long productId, MultipartFile[] images);

    boolean deleteProductImage(Long productId, String imageName);

    List<String> getProductImages(Long productId);
}