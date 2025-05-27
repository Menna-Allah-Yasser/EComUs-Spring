package org.iti.ecomus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "category")
@Data
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryId")
    private Long categoryId;

    @Column(name = "categoryName", nullable = false, unique = true)
    @NotEmpty
    private String categoryName;

    @ManyToMany
    @JoinTable(
        name = "productcategory",
        joinColumns = @JoinColumn(name = "categoryId"),
        inverseJoinColumns = @JoinColumn(name = "productId")
    )
    private List<Product> products;

    public Category() {}

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }
}