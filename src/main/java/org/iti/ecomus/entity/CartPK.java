package org.iti.ecomus.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;
@Data
public class CartPK implements Serializable {

    private int userId;
    private int productId;

    public CartPK() {
    }

    public CartPK(int userId, int productId) {
        this.userId = userId;
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartPK cartPK = (CartPK) o;
        return userId == cartPK.userId && productId == cartPK.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }
}

