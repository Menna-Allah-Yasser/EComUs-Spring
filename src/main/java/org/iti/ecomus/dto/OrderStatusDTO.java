package org.iti.ecomus.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.iti.ecomus.enums.OrderStatus;

@Data
public class OrderStatusDTO {

    @NotNull(message = "Order status is required")
    private OrderStatus status;

}