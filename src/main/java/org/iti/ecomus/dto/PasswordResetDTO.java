package org.iti.ecomus.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetDTO {
    @NotBlank
    private String token;
    
    @NotBlank
    private String newPassword;
}