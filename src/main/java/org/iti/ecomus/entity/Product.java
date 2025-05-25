package org.iti.ecomus.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "product")
@Data
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId")
    private int productId;

    @Column(name = "productName", nullable = false)
    private String productName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private int price;

    //    @Transient
    @ManyToMany
    @JoinTable(
            name = "productcategory",
            joinColumns = @JoinColumn(name = "productId"),
            inverseJoinColumns = @JoinColumn(name = "categoryId")
    )
    private List<Category> categories;

    public Product() {
    }

    public Product(String productName, String description, int quantity, int price) {
        this.productName = productName;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }


}
