package org.iti.ecomus.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Builder
@AllArgsConstructor
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderId")
    private Long orderId;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetails> orderDetails;

    public Order() {
    }

//    public Order(String address, BigDecimal price, Date date, OrderStatus status, PayType payType, User user) {
//        this.address = address;
//        this.price = price;
//        this.date = date;
//        this.status = status;
//        this.payType = payType;
//        this.user = user;
//    }

}
