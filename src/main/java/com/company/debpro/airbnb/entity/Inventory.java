package com.company.debpro.airbnb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_hotel_room_date",columnNames = {"hotel_id","room_id","date"}))
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)                 //Eager: when we are fetching an inventory, we want hotel info also else Lazy
    @JoinColumn(name = "hotel_id", nullable = false )
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)                 //Eager: when we are fetching an inventory, we want hotel info also else Lazy
    @JoinColumn(name = "room_id", nullable = false )
    private Room room;

    @Column
    private LocalDate date;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer bookedCount;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer totalCount;

    @Column(nullable = false,  precision = 2 ,scale = 2)      //scale: upto 99  and precision: after decimal 2 digits
    private BigDecimal surgeFactor;

    @Column(nullable = false,precision = 5, scale = 2)     //99999 , upto 2 decimal place
    private BigDecimal price;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private Boolean closed;

    @CreationTimestamp
    @Column(updatable = false)              //can't modify
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
