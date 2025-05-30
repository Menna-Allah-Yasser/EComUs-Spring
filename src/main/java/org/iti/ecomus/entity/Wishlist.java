package org.iti.ecomus.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "wishlist")
@IdClass(WishlistPK.class)
@Data
public class Wishlist implements Serializable {

    @Id
    @Column(name = "userId")
    private Long userId;

    @Id
    @Column(name = "productId")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    public Wishlist() {
    }

    public Wishlist(Long productId, Product product, User user) {
        this.productId = productId;
        this.product = product;
        this.user = user;
    }


}

