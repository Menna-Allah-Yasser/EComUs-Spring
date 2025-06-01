package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDTO(Product product);
    Product toProduct(ProductDTO productDTO);

    List<ProductDTO> toProductDTO(List<Product> products);
}
