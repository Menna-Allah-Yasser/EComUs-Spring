package org.iti.ecomus.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartDTO {
    @NotBlank(message = "Product ID must be provided")
    @Min(value = 1, message = "Product ID must be positive")
    private Long productId;
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;


}
