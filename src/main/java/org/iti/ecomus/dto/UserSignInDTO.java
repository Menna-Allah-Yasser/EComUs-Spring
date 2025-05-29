package org.iti.ecomus.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserSignInDTO {

    @NotBlank(message = "User name cannot be blank")
    private String userName;

    @NotBlank(message = "Password cannot be blank")
    private String password;


}
