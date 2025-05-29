package org.iti.ecomus.service.impl;

import org.iti.ecomus.dto.CheckOutOrderDTO;
import org.iti.ecomus.dto.OrderDTO;
import org.iti.ecomus.entity.Order;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.exceptions.OrderNotFoundException;
import org.iti.ecomus.mappers.OrderMapper;
import org.iti.ecomus.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderManagementService orderManagementService;

    @Autowired
    private CheckoutService checkoutService;

    @Transactional(readOnly = true)
    List<OrderDTO> getAllOrders() {
        return orderMapper.toOrderDTO(orderRepo.findAll());
    }


//    @Transactional
//    public OrderDTO saveOrder(OrderDTO orderDTO) {
//        return orderMapper.toOrderDTO(orderRepo.save(orderMapper.toOrder(orderDTO)));
//    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUserId(Long userId) {

        List<Order> orders = orderRepo.findByUser_UserId(userId);

        List<OrderDTO> orderDTOs = orderMapper.toOrderDTO(orders);


        return orderDTOs;
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {
       return orderMapper.toOrderDTO(
        orderManagementService.updateOrderStatus(orderId, status));

    }

    @Transactional
    public void deleteOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        orderRepo.delete(order);
    }

    @Transactional
    public OrderDTO createOrder(Long userId, CheckOutOrderDTO checkoutRequest) {
        return orderMapper.toOrderDTO(
        checkoutService.processCheckout(userId,checkoutRequest));
    }

}
