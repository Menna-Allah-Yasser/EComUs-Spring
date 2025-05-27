package org.iti.ecomus.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private int id;

    private String city;

    private String area;

    private String street;

    private String buildingNo;

}
