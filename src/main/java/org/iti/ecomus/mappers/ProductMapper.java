package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDTO(Product product);
    Product toProduct(ProductDTO productDTO);
}
