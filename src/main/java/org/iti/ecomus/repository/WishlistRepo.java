package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Cart;
import org.iti.ecomus.entity.Wishlist;
import org.iti.ecomus.entity.WishlistPK;
import org.iti.ecomus.paging.SearchRepository;
import org.iti.ecomus.specification.CartSpecification;
import org.iti.ecomus.specification.WishlistSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface WishlistRepo extends JpaRepository<Wishlist, WishlistPK>, SearchRepository<Wishlist, WishlistPK> {
    List<Wishlist> findByUserUserId(Long userId);

    @Override
    default Specification<Wishlist> getFiltersSpecification(String keyword, Map<String, Object> searchParams) {
        return WishlistSpecification.build(keyword, searchParams);
    }

}