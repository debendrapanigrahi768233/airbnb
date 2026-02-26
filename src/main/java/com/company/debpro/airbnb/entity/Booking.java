package com.company.debpro.airbnb.entity;

import com.company.debpro.airbnb.entity.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)                 //Eager: when we are fetching an inventory, we want hotel info also else Lazy
    @JoinColumn(name = "hotel_id", nullable = false )
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)                 //Eager: when we are fetching an inventory, we want hotel info also else Lazy
    @JoinColumn(name = "room_id", nullable = false )
    private Room room;

    @Column(nullable = false)
    private Integer roomsCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)    //so when we do .guest() then we will get the guest information
    @JoinTable(name = "booking_guests",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "guest_id"))
    private Set<Guest> guests;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus bookingStatus;

    @Column(nullable = false)
    private LocalDate checkInDate;

    @Column(nullable = false)
    private LocalDate checkOutDate;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

}
