package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Wishlist;
import org.iti.ecomus.entity.WishlistPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepo extends JpaRepository<Wishlist, WishlistPK> {
    List<Wishlist> findByUserUserId(Long userId);
}