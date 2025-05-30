package org.iti.ecomus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProfileDTO {
    @NotBlank(message = "User name cannot be empty")
    private String userName;

    @Email
    @NotBlank(message = "Email cannot be empty")
    private String email;

    private String job;

    private String creditNo;

    @DecimalMin(value = "0.0", inclusive = false, message = "Credit limit must be positive")
    private BigDecimal creditLimit;

    private String phone;
}
