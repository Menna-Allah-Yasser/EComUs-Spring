package org.iti.ecomus.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private int productId;

    private String productName;

    private String description;

    private int quantity;

    private int price;

    private List<CategoryDTO> categories;
}
