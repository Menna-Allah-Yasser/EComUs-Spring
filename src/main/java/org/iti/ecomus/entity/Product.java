package org.iti.ecomus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "product")
@Data
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

    @ManyToMany(mappedBy = "products") // "products" is the name of the field in Category
    private List<Category> categories;

    public Product() {}

    public Product(String productName, String description, int quantity, BigDecimal price) {
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }
}