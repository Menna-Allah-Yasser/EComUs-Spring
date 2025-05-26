package org.iti.ecomus.dto;


import lombok.*;
import org.iti.ecomus.entity.OrderDetails;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private int orderId;

    private String address;

    private int price;

    private Date date;

    private OrderStatus status;

    private PayType payType;

    private List<OrderDetailsDTO> orderDetails;
}
