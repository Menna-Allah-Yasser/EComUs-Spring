package org.iti.ecomus.controller.customer;

import jakarta.validation.Valid;
import org.iti.ecomus.dto.CheckOutOrderDTO;
import org.iti.ecomus.dto.OrderDTO;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/{id}")
    @PreAuthorize("@orderService.isOrderOwnedByUser(#id, #user.userId)")
    public ResponseEntity<OrderDTO> getOrderById(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders(@AuthenticationPrincipal User user) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(user.getUserId());
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal User user, @RequestBody @Valid CheckOutOrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(user.getUserId(), orderDTO);
        return ResponseEntity.ok(createdOrder);
    }

}
