package org.iti.ecomus.service.impl;

import org.iti.ecomus.entity.OrderDetails;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;
import org.iti.ecomus.exceptions.InvalidOrderStatusTransitionException;
import org.iti.ecomus.exceptions.OrderNotFoundException;
import org.iti.ecomus.repository.OrderDetailsRepo;
import org.iti.ecomus.repository.OrderRepo;
import org.iti.ecomus.repository.ProductRepo;
import org.iti.ecomus.repository.UserRepo;
import org.iti.ecomus.util.MailMessenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.iti.ecomus.entity.*;
import org.mockito.junit.jupiter.MockitoSettings;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@DisplayName("Order Management Service Tests")
@MockitoSettings(strictness = Strictness.LENIENT)
class OrderManagementServiceTest {

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private OrderDetailsRepo orderDetailsRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private MailMessenger mailMessenger;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private OrderManagementService orderManagementService;

    private Order testOrder;
    private User testUser;
    private Product testProduct1;
    private Product testProduct2;
    private List<OrderDetails> testOrderDetails;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUserName("testuser");
        testUser.setCreditLimit(BigDecimal.valueOf(1000));

        // Setup test products
        testProduct1 = new Product();
        testProduct1.setProductId(1L);
        testProduct1.setProductName("Test Product 1");
        testProduct1.setPrice(BigDecimal.valueOf(50.0));
        testProduct1.setQuantity(10);

        testProduct2 = new Product();
        testProduct2.setProductId(2L);
        testProduct2.setProductName("Test Product 2");
        testProduct2.setPrice(BigDecimal.valueOf(30.0));
        testProduct2.setQuantity(5);

        // Setup test order
        testOrder = new Order();
        testOrder.setOrderId(1L);
        testOrder.setUser(testUser);
        testOrder.setPrice(BigDecimal.valueOf(200));
        testOrder.setDate(Date.from(Instant.now()));
        testOrder.setStatus(OrderStatus.PROCESSING);
        testOrder.setPayType(PayType.CREDIT);
        testOrder.setAddress("Test Address");

        // Setup test order details
        OrderDetails orderDetail1 = new OrderDetails();
        orderDetail1.setOrderDetailsId(1L);
        orderDetail1.setOrder(testOrder);
        orderDetail1.setProduct(testProduct1);
        orderDetail1.setQuantity(2);
        orderDetail1.setPrice(BigDecimal.valueOf(50));

        OrderDetails orderDetail2 = new OrderDetails();
        orderDetail2.setOrderDetailsId(2L);
        orderDetail2.setOrder(testOrder);
        orderDetail2.setProduct(testProduct2);
        orderDetail2.setQuantity(3);
        orderDetail2.setPrice(BigDecimal.valueOf(30));

        testOrderDetails = Arrays.asList(orderDetail1, orderDetail2);
        testOrder.setOrderDetails(testOrderDetails);

    }

    @Nested
    @DisplayName("Valid Status Transitions")
    class ValidStatusTransitions {

        @Test
        @DisplayName("Should update order from PROCESSING to SHIPPED")
        void testProcessingToShipped() {
            // Given
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderRepo.save(any(Order.class))).thenReturn(testOrder);

            // When
            orderManagementService.updateOrderStatus(1L, OrderStatus.SHIPPED);

            // Then
            verify(orderRepo).save(testOrder);
            assertEquals(OrderStatus.SHIPPED, testOrder.getStatus());
        }

        @Test
        @DisplayName("Should update order from PROCESSING to CANCELED")
        void testProcessingToCanceled() {
            // Given
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderDetailsRepo.findOrderDetailsByOrderOrderId(1L)).thenReturn(testOrderDetails);
            when(productRepo.findById(1L)).thenReturn(Optional.of(testProduct1));
            when(productRepo.findById(2L)).thenReturn(Optional.of(testProduct2));
            when(orderRepo.save(any(Order.class))).thenReturn(testOrder);

            // When
            orderManagementService.updateOrderStatus(1L, OrderStatus.CANCELED);

            // Then
            verify(orderRepo).save(testOrder);
            assertEquals(OrderStatus.CANCELED, testOrder.getStatus());
            // Verify inventory restoration
            assertEquals(12, testProduct1.getQuantity()); // 10 + 2
            assertEquals(8, testProduct2.getQuantity());  // 5 + 3
            // Verify credit refund
            assertEquals(BigDecimal.valueOf(1200), testUser.getCreditLimit()); // 1000 + 200
        }

        @Test
        @DisplayName("Should update order from SHIPPED to COMPLETED")
        void testShippedToCompleted() {
            // Given
            testOrder.setStatus(OrderStatus.SHIPPED);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderRepo.save(any(Order.class))).thenReturn(testOrder);

            // When
            orderManagementService.updateOrderStatus(1L, OrderStatus.COMPLETED);

            // Then
            verify(orderRepo).save(testOrder);
            assertEquals(OrderStatus.COMPLETED, testOrder.getStatus());
        }

        @Test
        @DisplayName("Should update order from SHIPPED to CANCELED")
        void testShippedToCanceled() {
            // Given
            testOrder.setStatus(OrderStatus.SHIPPED);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderDetailsRepo.findOrderDetailsByOrderOrderId(1L)).thenReturn(testOrderDetails);
            when(productRepo.findById(1L)).thenReturn(Optional.of(testProduct1));
            when(productRepo.findById(2L)).thenReturn(Optional.of(testProduct2));
            when(orderRepo.save(any(Order.class))).thenReturn(testOrder);

            // When
            orderManagementService.updateOrderStatus(1L, OrderStatus.CANCELED);

            // Then
            verify(orderRepo).save(testOrder);
            assertEquals(OrderStatus.CANCELED, testOrder.getStatus());
        }
    }

    @Nested
    @DisplayName("Invalid Status Transitions")
    class InvalidStatusTransitions {

        @Test
        @DisplayName("Should throw exception when updating COMPLETED order to PROCESSING")
        void testCompletedToProcessing() {
            // Given
            testOrder.setStatus(OrderStatus.COMPLETED);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));

            // When & Then
            InvalidOrderStatusTransitionException exception = assertThrows(
                    InvalidOrderStatusTransitionException.class,
                    () -> orderManagementService.updateOrderStatus(1L, OrderStatus.PROCESSING)
            );

            assertTrue(exception.getMessage().contains("Cannot transition order from COMPLETED to PROCESSING"));
        }

        @Test
        @DisplayName("Should throw exception when updating COMPLETED order to SHIPPED")
        void testCompletedToShipped() {
            // Given
            testOrder.setStatus(OrderStatus.COMPLETED);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));

            // When & Then
            assertThrows(
                    InvalidOrderStatusTransitionException.class,
                    () -> orderManagementService.updateOrderStatus(1L, OrderStatus.SHIPPED)
            );
        }

        @Test
        @DisplayName("Should throw exception when updating CANCELED order")
        void testCanceledToAnyStatus() {
            // Given
            testOrder.setStatus(OrderStatus.CANCELED);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));

            // When & Then
            assertThrows(
                    InvalidOrderStatusTransitionException.class,
                    () -> orderManagementService.updateOrderStatus(1L, OrderStatus.PROCESSING)
            );

            assertThrows(
                    InvalidOrderStatusTransitionException.class,
                    () -> orderManagementService.updateOrderStatus(1L, OrderStatus.SHIPPED)
            );

            assertThrows(
                    InvalidOrderStatusTransitionException.class,
                    () -> orderManagementService.updateOrderStatus(1L, OrderStatus.COMPLETED)
            );
        }

        @Test
        @DisplayName("Should throw exception when updating PROCESSING to COMPLETED directly")
        void testProcessingToCompletedDirectly() {
            // Given
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));

            // When & Then
            assertThrows(
                    InvalidOrderStatusTransitionException.class,
                    () -> orderManagementService.updateOrderStatus(1L, OrderStatus.COMPLETED)
            );
        }
    }

    @Nested
    @DisplayName("Order Cancellation Logic")
    class OrderCancellationLogic {

        @Test
        @DisplayName("Should restore inventory when canceling order")
        void testInventoryRestoration() {
            // Given
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderDetailsRepo.findOrderDetailsByOrderOrderId(1L)).thenReturn(testOrderDetails);
            when(productRepo.findById(1L)).thenReturn(Optional.of(testProduct1));
            when(productRepo.findById(2L)).thenReturn(Optional.of(testProduct2));

            int originalQuantity1 = testProduct1.getQuantity();
            int originalQuantity2 = testProduct2.getQuantity();

            // When
            orderManagementService.updateOrderStatus(1L, OrderStatus.CANCELED);

            // Then
            verify(productRepo, times(2)).save(any(Product.class));
            assertEquals(originalQuantity1 + 2, testProduct1.getQuantity());
            assertEquals(originalQuantity2 + 3, testProduct2.getQuantity());
        }

        @Test
        @DisplayName("Should refund credit when canceling credit order")
        void testCreditRefund() {
            // Given
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderDetailsRepo.findOrderDetailsByOrderOrderId(1L)).thenReturn(testOrderDetails);
            when(productRepo.findById(anyLong())).thenReturn(Optional.of(testProduct1));

            BigDecimal originalCreditLimit = testUser.getCreditLimit();

            // When
            orderManagementService.updateOrderStatus(1L, OrderStatus.CANCELED);

            // Then
            verify(userRepo).save(testUser);
            assertEquals(originalCreditLimit.add( testOrder.getPrice()), testUser.getCreditLimit());
        }

        @Test
        @DisplayName("Should not refund credit when canceling cash order")
        void testNoCreditRefundForCashOrder() {
            // Given
            testOrder.setPayType(PayType.CASH);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderDetailsRepo.findOrderDetailsByOrderOrderId(1L)).thenReturn(testOrderDetails);
            when(productRepo.findById(anyLong())).thenReturn(Optional.of(testProduct1));

            BigDecimal originalCreditLimit = testUser.getCreditLimit();

            // When
            orderManagementService.updateOrderStatus(1L, OrderStatus.CANCELED);

            // Then
            verify(userRepo, never()).save(testUser);
            assertEquals(originalCreditLimit, testUser.getCreditLimit());
        }

        @Test
        @DisplayName("Should handle null credit limit gracefully")
        void testNullCreditLimitHandling() {
            // Given
            testUser.setCreditLimit(null);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));
            when(orderDetailsRepo.findOrderDetailsByOrderOrderId(1L)).thenReturn(testOrderDetails);
            when(productRepo.findById(anyLong())).thenReturn(Optional.of(testProduct1));

            // When & Then
            assertDoesNotThrow(() -> orderManagementService.updateOrderStatus(1L, OrderStatus.CANCELED));
            verify(userRepo, never()).save(testUser);
        }
    }

    @Nested
    @DisplayName("Exception Handling")
    class ExceptionHandling {

        @Test
        @DisplayName("Should throw OrderNotFoundException for non-existent order")
        void testOrderNotFound() {
            // Given
            when(orderRepo.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            OrderNotFoundException exception = assertThrows(
                    OrderNotFoundException.class,
                    () -> orderManagementService.updateOrderStatus(999L, OrderStatus.SHIPPED)
            );

            assertEquals("Order not found with id: 999", exception.getMessage());
        }

//        @Test
//        @DisplayName("Should throw exception when product not found during cancellation")
//        void testProductNotFoundDuringCancellation() {
//            // Given
//            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));
//            when(orderDetailsRepo.findOrderDetailsByOrderOrderId(1L)).thenReturn(testOrderDetails);
//            when(productRepo.findById(1L)).thenReturn(Optional.empty());
//
//            // When & Then
//            assertThrows(
//                    RuntimeException.class,
//                    () -> orderManagementService.updateOrderStatus(1L, OrderStatus.CANCELED)
//            );
//        }
    }

    @Nested
    @DisplayName("Utility Methods")
    class UtilityMethods {

        @Test
        @DisplayName("Should return true for valid status transitions")
        void testCanUpdateOrderStatusValid() {
            // Given
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));

            // When & Then
            assertTrue(orderManagementService.canUpdateOrderStatus(1L, OrderStatus.SHIPPED));
            assertTrue(orderManagementService.canUpdateOrderStatus(1L, OrderStatus.CANCELED));
        }

        @Test
        @DisplayName("Should return false for invalid status transitions")
        void testCanUpdateOrderStatusInvalid() {
            // Given
            testOrder.setStatus(OrderStatus.COMPLETED);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));

            // When & Then
            assertFalse(orderManagementService.canUpdateOrderStatus(1L, OrderStatus.PROCESSING));
            assertFalse(orderManagementService.canUpdateOrderStatus(1L, OrderStatus.SHIPPED));
        }

        @Test
        @DisplayName("Should return false for non-existent order")
        void testCanUpdateOrderStatusForNonExistentOrder() {
            // Given
            when(orderRepo.findById(999L)).thenReturn(Optional.empty());

            // When & Then
            assertFalse(orderManagementService.canUpdateOrderStatus(999L, OrderStatus.SHIPPED));
        }

        @Test
        @DisplayName("Should return allowed transitions for PROCESSING order")
        void testGetAllowedTransitionsForProcessing() {
            // Given
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));

            // When
            List<OrderStatus> allowedTransitions = orderManagementService.getAllowedStatusTransitions(1L);

            // Then
            assertEquals(2, allowedTransitions.size());
            assertTrue(allowedTransitions.contains(OrderStatus.SHIPPED));
            assertTrue(allowedTransitions.contains(OrderStatus.CANCELED));
        }

        @Test
        @DisplayName("Should return allowed transitions for SHIPPED order")
        void testGetAllowedTransitionsForShipped() {
            // Given
            testOrder.setStatus(OrderStatus.SHIPPED);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));

            // When
            List<OrderStatus> allowedTransitions = orderManagementService.getAllowedStatusTransitions(1L);

            // Then
            assertEquals(2, allowedTransitions.size());
            assertTrue(allowedTransitions.contains(OrderStatus.COMPLETED));
            assertTrue(allowedTransitions.contains(OrderStatus.CANCELED));
        }

        @Test
        @DisplayName("Should return empty list for COMPLETED order")
        void testGetAllowedTransitionsForCompleted() {
            // Given
            testOrder.setStatus(OrderStatus.COMPLETED);
            when(orderRepo.findById(1L)).thenReturn(Optional.of(testOrder));

            // When
            List<OrderStatus> allowedTransitions = orderManagementService.getAllowedStatusTransitions(1L);

            // Then
            assertTrue(allowedTransitions.isEmpty());
        }
    }
}