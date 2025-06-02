package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.CartDTO;
import org.iti.ecomus.entity.Cart;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring",uses ={ProductMapper.class})
public interface CartMapper {
    CartDTO toCartDTO(Cart cart);
    Cart toCart(CartDTO cartDTO);

    List<Cart> toCart(List<CartDTO> cartDTOs);
    List<CartDTO> toCartDTO(List<Cart> carts);
}
