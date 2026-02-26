package com.company.debpro.airbnb.repository;

import com.company.debpro.airbnb.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository         //If you wont provide the also fine as its an interface it wont create a bean of it and also we extending jpa repository
public interface HotelRepository extends JpaRepository<Hotel,Long> {
}
