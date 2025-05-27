package org.iti.ecomus.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.iti.ecomus.entity.OrderDetails;
import org.iti.ecomus.entity.Product;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsDTO {

    private int orderDetailsId;

    private ProductDTO product;

    private int quantity;

    private int price;


}
