package org.iti.ecomus.service.impl;

import lombok.RequiredArgsConstructor;
import org.iti.ecomus.dto.WishlistDTO;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.entity.Wishlist;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.repository.UserRepo;
import org.iti.ecomus.repository.WishlistRepo;
import org.iti.ecomus.service.WishlistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepo wishlistRepo;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;

    @Override
    public void addToWishlist(Long userId, Long productId) {
        if (!wishlistRepo.existsByUserIdAndProductId(userId, productId)) {
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productRepo.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            Wishlist wishlist = new Wishlist(productId, product, user);
            wishlist.setUserId(userId);
            wishlistRepo.save(wishlist);
        }
    }

    @Override
    public void removeFromWishlist(Long userId, Long productId) {
        wishlistRepo.deleteByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<WishlistDTO> getUserWishlist(Long userId) {
        return wishlistRepo.findByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isProductInWishlist(Long userId, Long productId) {
        return wishlistRepo.existsByUserIdAndProductId(userId, productId);
    }

    private WishlistDTO convertToDTO(Wishlist wishlist) {
        WishlistDTO dto = new WishlistDTO();
        dto.setUserId(wishlist.getUserId());
        dto.setProductId(wishlist.getProductId());
        dto.setProductName(wishlist.getProduct().getProductName());     // adjust based on your field name
//        dto.setProductPrice(wishlist.getProduct().getPrice());         // adjust based on your field name
//        dto.setProductImage(wishlist.getProduct().getProductImage());  // adjust based on your field name
        return dto;
    }
}