package com.company.debpro.airbnb.dto;

import com.company.debpro.airbnb.entity.User;
import com.company.debpro.airbnb.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class GuestDto {

    private Long id;
    private User user;
    private String name;
    private Gender gender;
    private Integer age;
}
