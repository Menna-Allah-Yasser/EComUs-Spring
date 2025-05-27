package org.iti.ecomus.repository;

import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.entity.OrderDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@IntegrationTest
public class OrderRepoTest {

    @Autowired
    private OrderRepo orderRepository;
    @Autowired
    private OrderDetailsRepo orderDetails;

    @Test
    void testFind() {
        // Given
        List<OrderDetails> orderDetailsList = orderDetails.findOrderDetailsByOrderOrderId(1L);
        System.out.println(orderDetailsList.size());
        orderDetailsList.forEach(orderDetails -> {
            System.out.println("Order Details ID: " + orderDetails.getOrderDetailsId());
            System.out.println("Product Name: " + orderDetails.getProduct().getProductName());
            System.out.println("Quantity: " + orderDetails.getQuantity());
        });
    }

    @Test
    void testDeleteOrderDetails() {
        // Given
        Integer deletedCount = orderDetails.deleteOrderDetailsByOrderOrderId(1L);
        System.out.println("Deleted Order Details Count: " + deletedCount);

        // Verify deletion
        List<OrderDetails> orderDetailsList = orderDetails.findOrderDetailsByOrderOrderId(1L);
        System.out.println("Remaining Order Details Count: " + orderDetailsList.size());
        assertEquals(0, orderDetailsList.size(), "Order details should be deleted successfully.");
    }
}
