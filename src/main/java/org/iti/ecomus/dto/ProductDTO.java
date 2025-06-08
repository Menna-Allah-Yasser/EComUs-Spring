package org.iti.ecomus.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long productId;

    @NotBlank(message = "productName cannot be blank")
    private String productName;

    @NotBlank(message = "description cannot be blank")
    private String description;

    @Min(1)
    private int quantity;

    @Min(1)
    private BigDecimal price;

    private List<CategoryNoProductDTO> categories;

    private List<String> images;

    public <T> ProductDTO(long l, String sampleProduct, String sampleDescription, int i, BigDecimal bigDecimal, List<T> sampleCategory) {
    }
}
