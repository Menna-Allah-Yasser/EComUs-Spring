package org.iti.ecomus.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class NewProductDTO {

    @NotBlank(message = "productName cannot be blank")
    private String productName;

    @NotBlank(message = "description cannot be blank")
    private String description;

    @Min(1)
    private int quantity;

    @Min(1)
    private BigDecimal price;

    private List<CategoryNameDTO> categories;
}
