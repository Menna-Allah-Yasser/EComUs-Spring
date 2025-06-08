package org.iti.ecomus.mappers;

import org.iti.ecomus.client.imageStorage.ImageStorageClient;
import org.iti.ecomus.client.imageStorage.LocalImageStorageClient;
import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring",uses={ImageMappingHelper.class})
public interface ProductMapper {

    @Mapping(target = "images", source = ".")
    ProductDTO toProductDTO(Product product);
    Product toProduct(ProductDTO productDTO);

    List<ProductDTO> toProductDTO(List<Product> products);
}
