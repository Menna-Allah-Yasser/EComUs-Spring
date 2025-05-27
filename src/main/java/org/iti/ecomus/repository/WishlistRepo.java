package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepo extends JpaRepository<Wishlist, Long> {
}
