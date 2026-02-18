package com.company.debpro.airbnb.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable           //we will embed this in the Hotel entity
public class HotelContactInfo {
    private String address; //In JPA/Hibernate, if you don’t specify @Column, Hibernate automatically: Creates a column for each field

    private String phoneNumber; //@Column is optional

    private String email;

    private String location;
}
