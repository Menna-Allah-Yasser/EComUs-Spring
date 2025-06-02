package org.iti.ecomus.dto;

import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WishlistDTO {
    private Long userId;
    private Long productId;
    private String productName;
    private Double productPrice;
    private String productImage;

}
