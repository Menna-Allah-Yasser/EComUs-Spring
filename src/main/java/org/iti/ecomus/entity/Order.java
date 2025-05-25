package org.iti.ecomus.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "orders")
@Data
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private int orderId;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(name = "date", nullable = false)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payType", nullable = false)
    private PayType payType;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    @NotEmpty
    private User user;

    public Order() {
    }

    public Order(String address, int price, Date date, OrderStatus status, PayType payType, User user) {
        this.address = address;
        this.price = price;
        this.date = date;
        this.status = status;
        this.payType = payType;
        this.user = user;
    }


}

