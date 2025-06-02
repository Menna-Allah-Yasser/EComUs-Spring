package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Wishlist;
import org.iti.ecomus.entity.WishlistPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepo extends JpaRepository<Wishlist, WishlistPK> {
    // Find all wishlist items for a specific user
    List<Wishlist> findByUserId(Long userId);

    // Check if a product exists in a user's wishlist
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    // Delete a specific wishlist item
    void deleteByUserIdAndProductId(Long userId, Long productId);
}