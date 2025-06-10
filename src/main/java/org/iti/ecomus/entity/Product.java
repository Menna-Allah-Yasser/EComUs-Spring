package org.iti.ecomus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Getter
@Setter

public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private Long productId;

    @Column(name = "productName", nullable = false)
//    @NotEmpty
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", nullable = false)
//    @Min(0)
    private int quantity;

    @Column(name = "price", nullable = false)
//    @Min(1)
    private BigDecimal price;

    @Formula("(SELECT COALESCE(SUM(od.quantity), 0) FROM orderdetails od WHERE od.productId = productId)")
    private Long purchaseCount;

    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    @JoinTable(
        name = "productcategory",
        joinColumns = @JoinColumn(name = "productId"),
        inverseJoinColumns = @JoinColumn(name = "categoryId")
    )
    @JsonBackReference // Avoid recursion during serialization
    private List<Category> categories;

    public Product() {}

    public Product(String productName, String description, int quantity, BigDecimal price) {
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
}