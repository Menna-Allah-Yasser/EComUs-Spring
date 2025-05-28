package org.iti.ecomus.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "cart")
@IdClass(CartPK.class)
@Data
public class Cart implements Serializable {

    @Id
    @Column(name = "userId")
    private Long userId;

    @Id
    @Column(name = "productId")
    private Long productId;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    public Cart() {
    }

    public Cart( Long productId, int quantity, Product product, User user) {
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
        this.user = user;
    }

}

