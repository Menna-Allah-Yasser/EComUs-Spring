package org.iti.ecomus.entity;

// todo --> password hash
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;


import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "user")
@Data
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "userName", nullable = false )
    @NotEmpty
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotEmpty
    private String email;

    @Column(name = "password", nullable = false)
    @NotEmpty
    private String password;

    @Column(name = "birthdate")
    private Date BD;

    @Column(name = "job")
    private String job;

    @Column(name = "creditNo")
    private String creditNo;

    @Column(name = "creditLimit")
    private Integer creditLimit;

    @Column(name = "phone")
    private String phone;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true ,
            fetch = FetchType.LAZY)
    private List<Address> addresses;

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true ,
            fetch = FetchType.LAZY)
    private List<Order> orders;

        @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true ,
            fetch = FetchType.LAZY)
    private List<Cart> carts;


    public User() {
    }

    public User(
            String userName, String email, String password, Date BD, String job, String creditNo, Integer creditLimit, String phone) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.BD = BD;
        this.job = job;
        this.creditNo = creditNo;
        this.creditLimit = creditLimit;
        this.phone = phone;
    }


}