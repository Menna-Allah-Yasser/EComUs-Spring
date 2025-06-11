package org.iti.ecomus.service.impl;

import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.iti.ecomus.client.imageStorage.ImageStorageClient;
import org.iti.ecomus.dto.NewProductDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Category;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.exceptions.BadRequestException;
import org.iti.ecomus.exceptions.ProductNotFoundException;
import org.iti.ecomus.mappers.NewProductMapper;
import org.iti.ecomus.mappers.ProductMapper;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.repository.CategoryRepo;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.service.ProductService;
import org.iti.ecomus.util.Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private NewProductMapper newProductMapper;

    @Autowired
    private ImageStorageClient imageStorageClient;

    @Autowired
    private Uploader uploader;

    @Override
    public ProductDTO getProductById(Long productId){
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        return productMapper.toProductDTO(product);
    }

    @Override
    public PagedResponse<ProductDTO> getAllProducts(PagingAndSortingHelper helper, int pageNum, int pageSize) {
        PagedResponse<Product> pagedResponse = helper.getPagedResponse(pageNum, pageSize, productRepo,null);
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
    public void uploadProductImages(Long productId, MultipartFile[] images) {
        // Validate image types first
        for (MultipartFile image : images) {
            if (image == null || image.isEmpty()) {
                continue;
            }

            String contentType = image.getContentType();
            if (contentType == null || !SUPPORTED_IMAGE_TYPES.contains(contentType)) {
                throw new BadRequestException("Unsupported file type: " + contentType);
            }
        }

        // Create a list to store image data
        List<Uploader.ImageData> imageDataList = new ArrayList<>();

        // Read all image data immediately
        for (MultipartFile image : images) {
            if (image == null || image.isEmpty()) {
                continue;
            }

            try {
                byte[] bytes = image.getBytes();
                if (bytes.length > 0) {
                    imageDataList.add(new Uploader.ImageData(
                        image.getOriginalFilename(),
                        bytes,
                        image.getSize()
                    ));
                }
            } catch (IOException e) {
                log.error("Failed to read image: {}", image.getOriginalFilename(), e);
            }
        }

        // Pass the pre-read image data to the uploader
        uploader.batchUploadAsync("product", productId, imageDataList);
    }

    @Override
    public boolean deleteProductImage(Long productId, String imageName) {
        return imageStorageClient.deleteImage("product"+"/"+ productId.toString()+"/"+ imageName);
    }

    private static final Set<String> SUPPORTED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/gif",
            "image/webp",
            "image/bmp",
            "image/svg+xml",
            "image/tiff"
    );

    @Override
    public List<String> getProductImages(Long productId) {
//        String prefix = "product/" + productId + "/";
        return imageStorageClient.getAllImages("product", productId.toString());
    }

    

    @Override
    @Transactional
    public ProductDTO addProductWithCategories(NewProductDTO newProductDTO) {
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

        return productMapper.toProductDTO( savedProduct);
    }
}