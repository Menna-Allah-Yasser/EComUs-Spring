package org.iti.ecomus.controller.customer;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.CheckOutOrderDTO;
import org.iti.ecomus.dto.OrderDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.iti.ecomus.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/orders")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Customer - Orders", description = "Customer order management")
@Validated
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("@orderService.isOrderOwnedByUser(#id, #user.userId)")
    public ResponseEntity<OrderDTO> getOrderById(@AuthenticationPrincipal User user, @PathVariable("id") @Min(1) Long id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<OrderDTO>> getOrders(@AuthenticationPrincipal User user, @PagingAndSortingParam(
                                                                     model = AppConstants.ORDER_MODEL,
                                                                     isUser = true,
                                                                     defaultSortField = "orderId"
                                                             ) PagingAndSortingHelper helper,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        return ResponseEntity.ok(orderService.getAllOrders(helper, pageNum, pageSize, user.getUserId()));
    }

//    @GetMapping
//    public ResponseEntity<List<OrderDTO>> getAllOrders(@AuthenticationPrincipal User user) {
//        List<OrderDTO> orders = orderService.getOrdersByUserId(user.getUserId());
//        return ResponseEntity.ok(orders);
//    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal User user, @RequestBody @Valid CheckOutOrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(user.getUserId(), orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

}
