package org.iti.ecomus.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long productId;

    private String productName;

    private String description;

    private int quantity;

    private BigDecimal price;

    private List<CategoryNoProductDTO> categories;
}
