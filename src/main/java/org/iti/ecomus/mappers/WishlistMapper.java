package org.iti.ecomus.mappers;

import org.iti.ecomus.dto.WishlistDTO;
import org.iti.ecomus.entity.Wishlist;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    WishlistDTO toWishlistDTO(Wishlist wishlist);
    Wishlist toWishlist(WishlistDTO wishlistDTO);
}
