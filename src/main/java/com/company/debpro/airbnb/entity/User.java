package com.company.debpro.airbnb.entity;

import com.company.debpro.airbnb.entity.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)           //unique level constraint at column level we put here, and postgres will create index for it
    private String email;

    @Column(nullable = false)
    private String password;  //encoded password will be stored here

    @Column(nullable = true)
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)       //This will create another table for us called, app_user_roles abd here all roles, user can have is there, one to many mapping
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;




}

