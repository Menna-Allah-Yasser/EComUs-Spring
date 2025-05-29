package org.iti.ecomus.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.iti.ecomus.entity.Order;
import org.iti.ecomus.entity.OrderDetails;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;
import org.iti.ecomus.exceptions.InvalidOrderStatusTransitionException;
import org.iti.ecomus.exceptions.OrderNotFoundException;
import org.iti.ecomus.repository.OrderDetailsRepo;
import org.iti.ecomus.repository.OrderRepo;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.repository.UserRepo;
import org.iti.ecomus.util.MailMessenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
public class OrderManagementService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailsRepo orderDetailsRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailMessenger mailMessenger;


    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));

        OrderStatus currentStatus = order.getStatus();

        // Validate status transition
        if (!currentStatus.canTransitionTo(newStatus)) {
            throw new InvalidOrderStatusTransitionException(
                    String.format("Cannot transition order from %s to %s. Allowed transitions from %s are: %s",
                            currentStatus, newStatus, currentStatus, currentStatus.getAllowedTransitions()));
        }

        // Handle specific status changes
        switch (newStatus) {
            case CANCELED:
                handleOrderCancellation(order);
                break;
            case COMPLETED:
                handleOrderCompletion(order);
                break;
            case SHIPPED:
                handleOrderShipment(order);
                break;
            case PROCESSING:
                // This should never happen due to validation above, but keeping for completeness
                log.warn("Attempting to set order {} back to PROCESSING from {}", orderId, currentStatus);
                break;
        }

        // Update the order status
        order.setStatus(newStatus);
        Order save = orderRepo.save(order);
        log.info("Order {} status updated from {} to {}", orderId, currentStatus, newStatus);
        return save;
    }

    private void handleOrderCancellation(Order order) {
        log.info("Processing cancellation for order {}", order.getOrderId());

        // Restore inventory
        restoreInventory(order);

        // Refund credit if applicable
        if (order.getPayType() == PayType.CREDIT) {
            refundCreditPayment(order);
        }

        log.info("Order {} successfully canceled. Inventory restored and payment refunded if applicable.",
                order.getOrderId());
    }

    private void handleOrderShipment(Order order) {
        log.info("Processing shipment for order {}", order.getOrderId());

        // Notify user via email
        mailMessenger.orderShipped(order.getUser().getUsername(), order.getUser().getEmail(), order.getOrderId().toString(),order.getDate().toString());

        log.info("Order {} successfully marked as shipped", order.getOrderId());
    }

    private void handleOrderCompletion(Order order) {
        log.info("Processing completion for order {}", order.getOrderId());

        // Notify user via email
        mailMessenger.orderCompleted(order.getUser().getUsername(), order.getUser().getEmail(), order.getOrderId().toString(), order.getDate().toString());

        log.info("Order {} successfully completed", order.getOrderId());
    }

    private void restoreInventory(Order order) {
        List<OrderDetails> orderDetailsList = order.getOrderDetails();

        for (OrderDetails orderDetails : orderDetailsList) {
            Product product = orderDetails.getProduct();

            // Restore the quantity back to inventory
            int restoredQuantity = product.getQuantity() + orderDetails.getQuantity();
            product.setQuantity(restoredQuantity);
            productRepo.save(product);

            log.debug("Restored {} units of product {} (ID: {}). New quantity: {}",
                    orderDetails.getQuantity(),
                    product.getProductName(),
                    product.getProductId(),
                    restoredQuantity);
        }
    }

    private void refundCreditPayment(Order order) {
        User user = order.getUser();
        BigDecimal currentCreditLimit = user.getCreditLimit();

        if (currentCreditLimit != null) {
            // Restore the credit limit
            user.setCreditLimit(currentCreditLimit.add( order.getPrice()));
            userRepo.save(user);

            log.debug("Refunded ${} to user {} (ID: {}). New credit limit: ${}",
                    order.getPrice(),
                    user.getUsername(),
                    user.getUserId(),
                    user.getCreditLimit());
        } else {
            log.warn("User {} has null credit limit, cannot refund credit payment for order {}",
                    user.getUserId(), order.getOrderId());
        }
    }

    // Additional utility methods
    public boolean canUpdateOrderStatus(Long orderId, OrderStatus newStatus) {
        try {
            Order order = orderRepo.findById(orderId)
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
            return order.getStatus().canTransitionTo(newStatus);
        } catch (OrderNotFoundException e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<OrderStatus> getAllowedStatusTransitions(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        return List.copyOf(order.getStatus().getAllowedTransitions());
    }
}