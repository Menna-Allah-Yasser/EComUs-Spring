package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",uses ={ProductMapper.class})
public interface CartMapper {
    CartDTO toCartDTO(Cart cart);
    Cart toCart(CartDTO cartDTO);
}
