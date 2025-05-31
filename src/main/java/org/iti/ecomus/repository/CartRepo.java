package org.iti.ecomus.repository;

import java.util.List;

import org.iti.ecomus.entity.Cart;
import org.iti.ecomus.entity.CartPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CartRepo extends JpaRepository<Cart, CartPK> {

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
}
