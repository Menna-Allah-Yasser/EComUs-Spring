package org.iti.ecomus.controller;

import lombok.RequiredArgsConstructor;
import org.iti.ecomus.dto.WishlistDTO;
import org.iti.ecomus.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/{userId}/{productId}")
    public ResponseEntity<Void> addToWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        wishlistService.addToWishlist(userId, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<Void> removeFromWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        wishlistService.removeFromWishlist(userId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<WishlistDTO>> getUserWishlist(@PathVariable Long userId) {
        List<WishlistDTO> wishlist = wishlistService.getUserWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }

    @GetMapping("/{userId}/{productId}")
    public ResponseEntity<Boolean> isProductInWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId) {
        boolean exists = wishlistService.isProductInWishlist(userId, productId);
        return ResponseEntity.ok(exists);
    }
}