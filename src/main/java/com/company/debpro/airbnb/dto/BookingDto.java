package com.company.debpro.airbnb.dto;

import com.company.debpro.airbnb.entity.Guest;
import com.company.debpro.airbnb.entity.Hotel;
import com.company.debpro.airbnb.entity.Room;
import com.company.debpro.airbnb.entity.User;
import com.company.debpro.airbnb.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {

    private Long id;
//    private Hotel hotel;
//    private Room room;
    private Integer roomsCount;
//    private User user;
    private Set<GuestDto> guests;
    private BookingStatus bookingStatus;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
