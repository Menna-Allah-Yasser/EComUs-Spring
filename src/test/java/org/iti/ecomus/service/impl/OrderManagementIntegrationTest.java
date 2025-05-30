package org.iti.ecomus.service.impl;

import jakarta.persistence.EntityManager;
import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.entity.Order;
import org.iti.ecomus.entity.OrderDetails;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;
import org.iti.ecomus.repository.OrderDetailsRepo;
import org.iti.ecomus.repository.OrderRepo;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.repository.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("Order Management Integration Tests")
class OrderManagementIntegrationTest {

    @Autowired
    private OrderManagementService orderManagementService;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailsRepo orderDetailsRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EntityManager entityManager;

    private User testUser;
    private Product testProduct;
    private Order testOrder;

    @BeforeEach
    void setUp() {
        // Create and save test user
        testUser = new User();
        testUser.setUserName("integrationuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setCreditLimit(BigDecimal.valueOf(1000));
        testUser = userRepo.save(testUser);

        // Create and save test product
        testProduct = new Product();
        testProduct.setProductName("Integration Test Product");
        testProduct.setDescription("Integration Test Product");
        testProduct.setPrice(BigDecimal.valueOf(100.0));
        testProduct.setQuantity(20);
        testProduct = productRepo.save(testProduct);

        // Create and save test order
        testOrder = new Order();
        testOrder.setUser(testUser);
        testOrder.setPrice(BigDecimal.valueOf(300));
        testOrder.setDate(Date.from(Instant.now()));
        testOrder.setStatus(OrderStatus.PROCESSING);
        testOrder.setPayType(PayType.CREDIT);
        testOrder.setAddress("Integration Test Address");
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrder(testOrder);
        orderDetails.setProduct(testProduct);
        orderDetails.setQuantity(3);
        orderDetails.setPrice(BigDecimal.valueOf(100));
        testOrder.setOrderDetails(new ArrayList<>(Arrays.asList((orderDetails))));
        testOrder = orderRepo.save(testOrder);

        // Create and save order details
//        orderDetailsRepo.save(orderDetails);
    }

    @Test
    @DisplayName("Should complete full order lifecycle")
    void testFullOrderLifecycle() {
        // Processing -> Shipped
        orderManagementService.updateOrderStatus(testOrder.getOrderId(), OrderStatus.SHIPPED);
        Order updatedOrder = orderRepo.findById(testOrder.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.SHIPPED, updatedOrder.getStatus());

        // Shipped -> Completed
        orderManagementService.updateOrderStatus(testOrder.getOrderId(), OrderStatus.COMPLETED);
        updatedOrder = orderRepo.findById(testOrder.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
    }

    @Test
    @DisplayName("Should handle order cancellation with inventory and credit restoration")
    void testOrderCancellationIntegration() {
        // Get initial values
        int initialProductQuantity = testProduct.getQuantity();
        BigDecimal initialCreditLimit = testUser.getCreditLimit();

        // Cancel order
        orderManagementService.updateOrderStatus(testOrder.getOrderId(), OrderStatus.CANCELED);

        // Verify order status
        Order canceledOrder = orderRepo.findById(testOrder.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.CANCELED, canceledOrder.getStatus());

        // Verify inventory restoration
        Product updatedProduct = productRepo.findById(testProduct.getProductId()).orElseThrow();
        assertEquals(initialProductQuantity + 3, updatedProduct.getQuantity());

        // Verify credit restoration
        User updatedUser = userRepo.findById(testUser.getUserId()).orElseThrow();
        assertEquals(initialCreditLimit.add( testOrder.getPrice()), updatedUser.getCreditLimit());
    }

    @Test
    @DisplayName("Should handle multiple order details during cancellation")
    void testMultipleOrderDetailsCancellation() {
        // Create another product and order detail
        Product product2 = new Product();
        product2.setProductName("Second Product");
        product2.setDescription("Second Product Description");
        product2.setPrice(BigDecimal.valueOf(50.0));
        product2.setQuantity(15);
        product2 = productRepo.save(product2);
        OrderDetails orderDetails2 = new OrderDetails();
        orderDetails2.setOrder(testOrder);
        orderDetails2.setProduct(product2);
        orderDetails2.setQuantity(2);
        orderDetails2.setPrice(BigDecimal.valueOf(50));
        testOrder = orderRepo.findById(testOrder.getOrderId()).orElseThrow();

//        testOrder.getOrderDetails().add(orderDetails2);
        orderDetailsRepo.save(orderDetails2);
        orderDetailsRepo.flush();
        entityManager.clear();
        testOrder = orderRepo.findById(testOrder.getOrderId()).orElseThrow();
        OrderDetails orderDetails3 = orderDetailsRepo.findById(orderDetails2.getOrderDetailsId()).get();
        // Get initial quantities
        int initialQuantity1 = testProduct.getQuantity();
        int initialQuantity2 = product2.getQuantity();

        // Cancel order
        orderManagementService.updateOrderStatus(testOrder.getOrderId(), OrderStatus.CANCELED);

        // Verify both products had inventory restored
        Product updatedProduct1 = productRepo.findById(testProduct.getProductId()).orElseThrow();
        Product updatedProduct2 = productRepo.findById(product2.getProductId()).orElseThrow();

        assertEquals(initialQuantity1 + 3, updatedProduct1.getQuantity());
        assertEquals(initialQuantity2 + 2, updatedProduct2.getQuantity());
    }
}