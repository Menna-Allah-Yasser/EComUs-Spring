package org.iti.ecomus.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private Long id;

    private String city;

    private String area;

    private String street;

    private String buildingNo;

    private Long userId;

}
