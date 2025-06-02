package org.iti.ecomus.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.iti.ecomus.util.validation.OnUpdate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    @NotNull(message = "Address ID is required", groups = OnUpdate.class)
    private Long id;

    @NotNull(message = "Address city is required")
    private String city;

    @NotNull(message = "Address area is required")
    private String area;

    @NotNull(message = "Address street is required")
    private String street;

    @NotNull(message = "Address building number is required")
    private String buildingNo;

}
