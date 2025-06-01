package org.iti.ecomus.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.util.validation.OnUpdate;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @NotNull(message = "controller Id required" , groups = OnUpdate.class)
    private Long categoryId;

    @NotNull(message = "controller name required")
    private String categoryName;

    List<ProductDTO> products;

}
