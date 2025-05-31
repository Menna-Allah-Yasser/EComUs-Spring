package org.iti.ecomus.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutOrderDTO {

    @NotBlank(message = "an order must have an address")
    private String address;

    @NotNull(message = "payment method is required")
    private PayType payType;

}
