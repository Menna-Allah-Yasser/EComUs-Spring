package org.iti.ecomus.controller.admin;

import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.iti.ecomus.config.AppConstants;
import org.iti.ecomus.dto.OrderDTO;
import org.iti.ecomus.dto.OrderStatusDTO;
import org.iti.ecomus.dto.PagedResponse;
import org.iti.ecomus.entity.User;
import org.iti.ecomus.paging.PagingAndSortingHelper;
import org.iti.ecomus.paging.PagingAndSortingParam;
import org.iti.ecomus.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class OrderAdminController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<PagedResponse<OrderDTO>> getOrders(@AuthenticationPrincipal User user, @PagingAndSortingParam(
                                                                     model = AppConstants.ORDER_MODEL,
                                                                     defaultSortField = "orderId"
                                                             ) PagingAndSortingHelper helper,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        return ResponseEntity.ok(orderService.getAllOrders(helper, pageNum, pageSize, user.getUserId()));
    }

    @PutMapping("/{id}/update-status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable("id") Long id, @RequestBody @Valid OrderStatusDTO orderDTO) {

        return ResponseEntity.ok(orderService.updateOrderStatus(id, orderDTO.getStatus()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

}
