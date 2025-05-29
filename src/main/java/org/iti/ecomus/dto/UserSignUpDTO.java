package org.iti.ecomus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignUpDTO {

    private String userName;

    private String email;

    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date BD;

    private String job;

    private String creditNo;

    private Integer creditLimit;

    private String phone;

}

