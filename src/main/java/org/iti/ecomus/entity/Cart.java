package org.iti.ecomus.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;


@Entity
@Table(name = "cart")
@IdClass(CartPK.class)
@Data
public class Cart implements Serializable {

    @Id
    @Column(name = "userId")
    private int userId;

    @Id
    @Column(name = "productId")
    private int productId;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    private Product product;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    public Cart() {
    }

    public Cart( int productId, int quantity, Product product, User user) {
        this.productId = productId;
        this.quantity = quantity;
        this.product = product;
        this.user = user;
    }

}

