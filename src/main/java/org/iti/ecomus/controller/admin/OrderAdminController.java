package org.iti.ecomus.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Admin - Orders", description = "Admin order management")
@Validated
public class OrderAdminController {

    @Autowired
    private OrderService orderService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedResponse<OrderDTO>> getOrders(@AuthenticationPrincipal User user, @PagingAndSortingParam(
                                                                     model = AppConstants.ORDER_MODEL,
                                                                     defaultSortField = "orderId"
                                                             ) PagingAndSortingHelper helper,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNum,
                                                             @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize) {
        return ResponseEntity.ok(orderService.getAllOrders(helper, pageNum, pageSize, user.getUserId()));
    }

    @PutMapping(path = "/{id}/update-status",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable("id") @Min(1) Long id, @RequestBody @Valid OrderStatusDTO orderDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.updateOrderStatus(id, orderDTO.getStatus()));
    }

    @GetMapping(path = "/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable("id") @Min(1) Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

}
