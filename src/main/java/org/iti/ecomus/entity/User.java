package org.iti.ecomus.entity;

// todo --> password hash
import jakarta.persistence.*;
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

    @Column(name = "userName", nullable = false)
    private String userName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "BD")
    private Date BD;

    @Column(name = "job")
    private String job;

    @Column(name = "city")
    private String city;

    @Column(name = "area")
    private String area;

    @Column(name = "street")
    private String street;

    @Column(name = "buildingNo")
    private String buildingNo;

    @Column(name = "creditNo")
    private String creditNo;

    @Column(name = "creditLimit")
    private Integer creditLimit;

    @Column(name = "phone")
    private String phone;

//    @OneToMany(mappedBy = "user",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true ,
//            fetch = FetchType.LAZY)
//    private List<Order> orders;

    public User() {
    }

    public User( String userName, String email, String password, Date BD, String job, String city, String area, String street, String buildingNo, String creditNo, Integer creditLimit, String phone , List<Order> orders) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.BD = BD;
        this.job = job;
        this.city = city;
        this.area = area;
        this.street = street;
        this.buildingNo = buildingNo;
        this.creditNo = creditNo;
        this.creditLimit = creditLimit;
        this.phone = phone;
//        this.orders=orders;
    }


}