package com.company.debpro.airbnb.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // 1,2,3,4.....
    private Long id;

    @Column(nullable = false)
    private String name;

    private String city;

    @Column(columnDefinition = "Text[]")
    private String[] photos;

    @Column(columnDefinition = "Text[]")
    private String[] amenities;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Embedded
    private HotelContactInfo contactInfo;                 //contact_info_address, contact_info_phone_number, ....

    @Column(nullable = false)
    private Boolean active;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User owner;

    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;
}
