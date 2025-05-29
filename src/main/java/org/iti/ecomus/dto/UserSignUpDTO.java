package org.iti.ecomus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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

    @NotBlank(message = "User name cannot be empty")
    private String userName;

    @Email
    @NotBlank(message = "Email cannot be empty")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date BD;

    private String job;

    private String creditNo;

    private Integer creditLimit;

    private String phone;

}

