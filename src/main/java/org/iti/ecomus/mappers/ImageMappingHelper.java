package org.iti.ecomus.mappers;

import org.iti.ecomus.client.imageStorage.ImageStorageClient;
import org.iti.ecomus.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageMappingHelper {

    @Autowired
    private ImageStorageClient imageStorageClient;

    // MapStruct recognizes this as a mapping method: Product -> List<String>
    public List<String> productToImagePaths(Product product) {
        if (product != null && product.getProductId() != null) {
            return imageStorageClient.getAllImages("product", product.getProductId().toString());
        }
        return new ArrayList<>();
    }

    // You can also add other mapping methods if needed
    public String productIdToString(Long productId) {
        return productId != null ? productId.toString() : null;
    }
}
