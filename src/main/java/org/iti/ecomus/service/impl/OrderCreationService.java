package org.iti.ecomus.service.impl;

import org.iti.ecomus.entity.Cart;
import org.iti.ecomus.entity.Order;
import org.iti.ecomus.entity.OrderDetails;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;
import org.iti.ecomus.exceptions.CartEmptyException;
import org.iti.ecomus.repository.CartRepo;
import org.iti.ecomus.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.iti.ecomus.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderCreationService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private CartRepo cartRepo;


    @Transactional
    public Order createOrder(User user, String fullAddress, PayType payment,BigDecimal totalPrice) {

        List<Cart> carts = user.getCarts();
        if (carts.isEmpty()) {
            throw new CartEmptyException("Cart is empty, cannot create order.");
        }
        Order order = Order.builder()
                .user(user)
                .address(fullAddress)
                .price(totalPrice)
                .date(Date.from(Instant.now()))
                .status(OrderStatus.PROCESSING)
                .payType(payment)
                .build();

        order.setOrderDetails(new ArrayList<>());
        // convert carts to order details and set them in the order
        for( Cart cart : carts) {
        OrderDetails orderDetails = OrderDetails.builder()
                .order(order)
                .product(cart.getProduct())
                .quantity(cart.getQuantity())
                .price(cart.getProduct().getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())))
                .build();
            order.getOrderDetails().add(orderDetails);

        }

        return orderRepo.save(order);
    }
}
