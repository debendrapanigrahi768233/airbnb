package com.company.debpro.airbnb.dto;

import com.company.debpro.airbnb.entity.HotelContactInfo;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class HotelDto {
    private Long id;
    private String name;
    private String city;
    private String[] photos;
    private String[] amenities;
    private HotelContactInfo contactInfo;                 //contact_info_address, contact_info_phone_number, ....
    private Boolean active;
}
