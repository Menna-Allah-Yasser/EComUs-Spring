package org.iti.ecomus.service.impl;

import org.iti.ecomus.dto.CheckOutOrderDTO;
import org.iti.ecomus.dto.OrderDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.dto.UserDTO;
import org.iti.ecomus.entity.Order;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.exceptions.OrderNotFoundException;
import org.iti.ecomus.exceptions.ResourceNotFoundException;
import org.iti.ecomus.mappers.OrderMapper;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.repository.OrderRepo;
import org.iti.ecomus.repository.UserRepo;
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
    @Autowired
    private UserRepo userRepo;

    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderMapper.toOrderDTO(orderRepo.findAll());
    }

    @Transactional(readOnly = true)
    public PagedResponse<OrderDTO> getAllOrders(PagingAndSortingHelper helper, int pageNum, int pageSize) {
        PagedResponse<Order> pagedResponse = helper.getPagedResponse(pageNum, pageSize, orderRepo);
        PagedResponse<OrderDTO> resp = pagedResponse.mapContent(orderMapper::toOrderDTO);
        return resp;
    }


    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id: " + orderId));
        return orderMapper.toOrderDTO(order);
    }

//    @Transactional
//    public OrderDTO saveOrder(OrderDTO orderDTO) {
//        return orderMapper.toOrderDTO(orderRepo.save(orderMapper.toOrder(orderDTO)));
//    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        if (userId == null || !userRepo.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        List<Order> orders = orderRepo.findByUser_UserId(userId);

        List<OrderDTO> orderDTOs = orderMapper.toOrderDTO(orders);


        return orderDTOs;
    }

    @Transactional
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus status) {

       return orderMapper.toOrderDTO(
        orderManagementService.updateOrderStatus(orderId, status));

    }

    @Transactional(readOnly = true)
    public boolean isOrderOwnedByUser(Long orderId, Long userId) {
        return orderRepo.existsByOrderIdAndUser_UserId(orderId, userId);
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
