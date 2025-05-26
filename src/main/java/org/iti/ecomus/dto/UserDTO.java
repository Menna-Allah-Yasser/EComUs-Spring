package org.iti.ecomus.dto;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iti.ecomus.entity.Cart;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private int userId;

    private String userName;

    private String email;

    private String password;

    private Date BD;

    private String job;

    private String creditNo;

    private Integer creditLimit;

    private String phone;

    private List<AddressDTO> addresses;


    private List<OrderDTO> orders;


    private List<CartDTO> carts;

}
