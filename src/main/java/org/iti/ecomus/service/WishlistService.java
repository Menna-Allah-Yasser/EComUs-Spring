package org.iti.ecomus.service;

import org.iti.ecomus.dto.WishlistDTO;
import java.util.List;

public interface WishlistService {
    void addToWishlist(Long userId, Long productId);
    void removeFromWishlist(Long userId, Long productId);
    List<WishlistDTO> getUserWishlist(Long userId);
    boolean isProductInWishlist(Long userId, Long productId);
}