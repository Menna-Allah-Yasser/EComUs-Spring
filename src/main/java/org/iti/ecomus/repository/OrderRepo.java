package org.iti.ecomus.repository;

import org.iti.ecomus.entity.Order;
import org.iti.ecomus.entity.OrderDetails;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.paging.SearchRepository;
import org.iti.ecomus.specification.OrderSpecification;
import org.iti.ecomus.specification.ProductSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface OrderRepo extends JpaRepository<Order, Long>, SearchRepository<Order, Long> {

    List<Order> findByUser_UserId(Long userId);

    boolean existsByOrderIdAndUser_UserId(Long orderId, Long userId);


    @Override
    default public Specification<Order> getFiltersSpecification(String keyword, Map<String, Object> searchParams) {
        return OrderSpecification.build(keyword, searchParams);
    }
}
