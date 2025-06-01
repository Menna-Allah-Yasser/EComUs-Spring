package org.iti.ecomus.dto;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    @JsonProperty("id")
    private Long productId;

    private String productName;

    private String description;

    private int quantity;

    private BigDecimal price;

    private List<CategoryNoProductDTO> categories;
}
