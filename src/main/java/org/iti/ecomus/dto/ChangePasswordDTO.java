package org.iti.ecomus.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO
{
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

}
