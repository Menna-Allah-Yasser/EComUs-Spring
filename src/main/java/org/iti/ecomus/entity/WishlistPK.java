package org.iti.ecomus.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class WishlistPK implements Serializable {

    private Long userId;
    private Long productId;

//    public WishlistPK(Long userId, Long productId) {
//        this.userId = userId;
//        this.productId = productId;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishlistPK that = (WishlistPK) o;
        return userId == that.userId && productId == that.productId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }
}

