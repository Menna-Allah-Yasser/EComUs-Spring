package org.iti.ecomus.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Embeddable
@Data
public class ProductCategoryPK implements Serializable {

    private int productId;
    private int categoryId;


    public ProductCategoryPK(int productId, int categoryId) {
        this.productId = productId;
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategoryPK that = (ProductCategoryPK) o;
        return productId == that.productId && categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, categoryId);
    }
}

