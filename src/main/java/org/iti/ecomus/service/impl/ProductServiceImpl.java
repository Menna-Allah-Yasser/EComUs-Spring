package org.iti.ecomus.service.impl;

import org.iti.ecomus.dto.NewProductDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.exceptions.ProductNotFoundException;
import org.iti.ecomus.mappers.NewProductMapper;
import org.iti.ecomus.mappers.ProductMapper;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.repository.CategoryRepo;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private NewProductMapper newProductMapper;
    

    @Override
    public ProductDTO getProductById(Long productId){
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        return productMapper.toProductDTO(product);
    }

    @Override
    public PagedResponse<ProductDTO> getAllProducts(PagingAndSortingHelper helper, int pageNum, int pageSize) {
        PagedResponse<Product> pagedResponse = helper.getPagedResponse(pageNum, pageSize, productRepo);
        PagedResponse<ProductDTO> resp = pagedResponse.mapContent(productMapper::toProductDTO);
        return resp;
    }

    public void deleteProductById(Long productId){
        productRepo.deleteById(productId);
    }

    public Optional<List<ProductDTO>> getProductsByQuantityGreaterThan(int quantity){
        return Optional.ofNullable(productMapper.toProductDTO(productRepo.findByQuantityGreaterThanEqual(quantity)));
    }


    @Override
    public ProductDTO updateProduct(Long id, NewProductDTO product){
        if(!productRepo.existsByProductId(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        Product product1 = newProductMapper.toProduct(product);
        product1.setCategories(new ArrayList<>());
        product.getCategories().forEach(category -> {
            Category existingCategory = categoryRepo.getCategoryByCategoryName(category.getCategoryName());
            if (existingCategory == null) {
                existingCategory = new Category();
                existingCategory.setCategoryName(category.getCategoryName());
                existingCategory = categoryRepo.save(existingCategory);
            }
            product1.getCategories().add(existingCategory);
        });
        product1.setProductId(id);

        return productMapper.toProductDTO(productRepo.save(product1));
    }

    @Override
    public List<ProductDTO> findByProductName(String name) {
        return productMapper.toProductDTO(productRepo.findByProductName(name));
    }

    

    @Override
    @Transactional
    public Long addProductWithCategories(NewProductDTO newProductDTO) {
        Product product = new Product();
        product.setProductName(newProductDTO.getProductName());
        product.setDescription(newProductDTO.getDescription());
        product.setQuantity(newProductDTO.getQuantity());
        product.setPrice(newProductDTO.getPrice());

        List<Category> productCategories = newProductDTO.getCategories()
            .stream()
            .map(categoryDTO -> {
                Category category = categoryRepo.getCategoryByCategoryName(categoryDTO.getCategoryName());

                if (category == null) {
                    category = new Category();
                    category.setCategoryName(categoryDTO.getCategoryName());
                    category = categoryRepo.save(category);
                }
                return category;
            })
            .toList();

        product.setCategories(productCategories);

        Product savedProduct = productRepo.save(product);

        return savedProduct.getProductId();
    }
}