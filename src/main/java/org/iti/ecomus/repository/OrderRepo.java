package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
