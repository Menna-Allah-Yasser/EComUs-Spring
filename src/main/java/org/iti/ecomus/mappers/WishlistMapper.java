package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.ProductDTO;
import org.iti.ecomus.dto.WishlistDTO;
import org.iti.ecomus.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring" , uses = {ProductMapper.class, UserMapper.class})
public interface WishlistMapper {
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.productName", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    WishlistDTO toDTO(Wishlist wishlist);


    Wishlist toEntity(WishlistDTO dto);

    List<WishlistDTO> toDTOList(List<Wishlist> entities);
    List<Wishlist> toEntityList(List<WishlistDTO> dtos);

    @Mapping(target = ".",source = "product")
    ProductDTO wishlistToProductDTO(Wishlist wishlist);


    List<ProductDTO> wishlistToProductDTO(List<Wishlist> wishlists);
}
