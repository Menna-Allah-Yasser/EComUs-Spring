package org.iti.ecomus.repository;

import java.util.List;
import java.util.Map;

import org.iti.ecomus.entity.Cart;
import org.iti.ecomus.entity.CartPK;
import org.iti.ecomus.entity.Order;
import org.iti.ecomus.paging.SearchRepository;
import org.iti.ecomus.specification.CartSpecification;
import org.iti.ecomus.specification.OrderSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepo extends JpaRepository<Cart, CartPK> , SearchRepository<Cart, CartPK> {

    List<Cart> findByUserUserId(Long userId);

    Cart findByUserUserIdAndProductProductId(Long userId, Long productId); 

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.userId = :userId AND c.product.productId = :productId")
    void deleteByUserUserIdAndProductProductId(Long userId, Long productId);

    @Modifying
    @Query("DELETE FROM Cart c WHERE c.user.userId = :userId")
    void deleteByUserUserId(Long userId);


    @Query("SELECT SUM(c.quantity) FROM Cart c WHERE c.user.userId = :userId")
    Integer getTotalQuantityByUserId(Long userId);

    @Query("SELECT SUM(c.quantity * c.product.price) FROM Cart c WHERE c.user.userId = :userId")
    Integer calculateCartTotal(Long userId);

    @Query("SELECT (c.quantity * c.product.price) FROM Cart c WHERE c.user.userId = :userId AND c.product.productId = :productId")
Integer calculateProductTotalInCart(@Param("userId") Long userId, @Param("productId") Long productId);


    @Override
    default Specification<Cart> getFiltersSpecification(String keyword, Map<String, Object> searchParams) {
        return CartSpecification.build(keyword, searchParams);
    }

}
