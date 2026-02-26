package com.company.debpro.airbnb.dto;

import com.company.debpro.airbnb.entity.Hotel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
public class RoomDto {
    private Long id;
    //since hotel was lazy ily imported so i am ignoring it if I don't need here
    private String type;
    private String basePrice;
    private String[] photos;
    private Integer totalCount;
    private Integer capacity;
    private String[] amenities;
}
