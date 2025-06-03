package org.iti.ecomus.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "wishlist")
@IdClass(WishlistPK.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist implements Serializable {

    @Id
    @Column(name = "userId")
    private Long userId;

    @Id
    @Column(name = "productId")
    private Long productId;

    @ManyToOne
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId", insertable = false, updatable = false)
    private Product product;


}

