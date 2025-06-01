package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.NewProductDTO;
import org.iti.ecomus.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewProductMapper {

    NewProductDTO toNewProductDTO(Product product);
    Product toProduct(NewProductDTO newProductDTO);
}
