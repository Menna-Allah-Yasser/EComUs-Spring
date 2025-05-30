package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Order;
import org.iti.ecomus.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepo extends JpaRepository<Order, Long> {

    List<Order> findByUser_UserId(Long userId);

    boolean existsByOrderIdAndUser_UserId(Long orderId, Long userId);
}
