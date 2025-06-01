package org.iti.ecomus.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressId")
    private Long id;

    @Column(name = "city")
    private String city;

    @Column(name = "area")
    private String area;

    @Column(name = "street")
    private String street;

    @Column(name = "buildingNo")
    private String buildingNo;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public Address() {
    }

    public Address(String city, String area, String street, String buildingNo) {
        this.city = city;
        this.area = area;
        this.street = street;
        this.buildingNo = buildingNo;
    }


}
