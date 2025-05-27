package org.iti.ecomus.mapper;

import org.iti.ecomus.annotation.IntegrationTest;
import org.iti.ecomus.dto.OrderDTO;
import org.iti.ecomus.entity.*;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;
import org.iti.ecomus.mappers.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
//@Import({TestContainersConfig.class})
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void testToOrderDTO() {
        // Given
        // Create a sample Order entity object
        Order order = new Order();
        order.setOrderId(1L);
        order.setPrice(new BigDecimal("99.99"));
        order.setStatus(OrderStatus.PROCESSING);
        Date date = new Date(2000, 0, 1); // January 1, 2000
        order.setDate(date);
        order.setPayType(PayType.CASH);
        order.setAddress("123 Main St, City, Country");

        User user = new User();
        order.setUser(user);
        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("ELECTRONICS");

        Product product = new Product();
        product.setProductId(1L);
        product.setProductName("Smartphone");
        product.setDescription("Latest model smartphone");
        product.setPrice(new BigDecimal("699.99"));
        product.setCategories(Arrays.asList(category));

        List<OrderDetails> orderDetailsList = List.of(
                new OrderDetails(order, product, 2, new BigDecimal("49.99"))
        );
        order.setOrderDetails(orderDetailsList);
        // When
        // Convert entity to DTO using OrderMapper
        OrderDTO orderDto = orderMapper.toOrderDTO(order);

        // Then
        assertNotNull(orderDto);
        assertEquals(1L, orderDto.getOrderId());
        assertEquals(new BigDecimal("99.99"), orderDto.getPrice());
        assertEquals(OrderStatus.PROCESSING, orderDto.getStatus());
        assertEquals(date, orderDto.getDate());
        assertEquals(PayType.CASH, orderDto.getPayType());
        assertEquals("123 Main St, City, Country", orderDto.getAddress());
        assertNotNull(orderDto.getOrderDetails());
        assertEquals(1, orderDto.getOrderDetails().size());
//        assertEquals(1L, orderDto.getOrderDetails().get(0).getOrderDetailsId());
        assertEquals(new BigDecimal("49.99"), orderDto.getOrderDetails().get(0).getPrice());
        assertEquals(2, orderDto.getOrderDetails().get(0).getQuantity());
        assertEquals("Smartphone", orderDto.getOrderDetails().get(0).getProduct().getProductName());
        assertEquals(new BigDecimal("699.99"), orderDto.getOrderDetails().get(0).getProduct().getPrice());
        assertEquals("Latest model smartphone", orderDto.getOrderDetails().get(0).getProduct().getDescription());
        assertEquals(1L, orderDto.getOrderDetails().get(0).getProduct().getProductId());
        assertEquals(1, orderDto.getOrderDetails().get(0).getProduct().getCategories().size());
        assertEquals("ELECTRONICS", orderDto.getOrderDetails().get(0).getProduct().getCategories().get(0).getCategoryName());


    }

    @Test
    void testToEntity() {
        // Given
        OrderDTO orderDto = new OrderDTO();
        orderDto.setOrderId(2L);
        orderDto.setPrice(new BigDecimal("150.00"));
        orderDto.setStatus(OrderStatus.PROCESSING);
        Date date = new Date(2022, 5, 15);
        orderDto.setDate(date);
        orderDto.setPayType(PayType.CASH);
        orderDto.setAddress("789 Oak St, City, Country");
        // Not setting orderDetails or other unmapped fields

        // When
        Order order = orderMapper.toOrder(orderDto);

        // Then
        assertNotNull(order);
        assertEquals(2L, order.getOrderId());
        assertEquals(new BigDecimal("150.00"), order.getPrice());
        assertEquals(OrderStatus.PROCESSING, order.getStatus());
        assertEquals(date, order.getDate());
        assertEquals(PayType.CASH, order.getPayType());
        assertEquals("789 Oak St, City, Country", order.getAddress());
    }
}
