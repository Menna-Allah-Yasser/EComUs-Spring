package org.iti.ecomus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "orderdetails")
@Data
@Builder
@AllArgsConstructor
public class OrderDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderDetailsId")
    private Long orderDetailsId;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    @Min(1)
    private int quantity;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public OrderDetails() {
    }

//    public OrderDetails(Order order, Product product, int quantity, BigDecimal price) {
//        this.order = order;
//        this.product = product;
//        this.quantity = quantity;
//        this.price = price;
//    }


}

