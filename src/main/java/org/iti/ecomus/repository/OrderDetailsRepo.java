package org.iti.ecomus.repository;

import org.iti.ecomus.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailsRepo extends JpaRepository<OrderDetails, Long> {

    List<OrderDetails> findOrderDetailsByOrderOrderId(Long orderId);
    Integer deleteOrderDetailsByOrderOrderId(Long orderId);

}
