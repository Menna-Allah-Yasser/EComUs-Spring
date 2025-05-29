package org.iti.ecomus.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iti.ecomus.enums.OrderStatus;
import org.iti.ecomus.enums.PayType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckOutOrderDTO {

    private String address;


    private PayType payType;

}
