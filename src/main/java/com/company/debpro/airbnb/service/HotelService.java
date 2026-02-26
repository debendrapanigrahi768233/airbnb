package com.company.debpro.airbnb.service;

import com.company.debpro.airbnb.dto.HotelDto;
import com.company.debpro.airbnb.dto.HotelInfoDto;
import com.company.debpro.airbnb.entity.Hotel;
import org.jspecify.annotations.Nullable;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
    HotelDto updateHotelById(Long id, HotelDto hotelDto);
    void deleteHotelById(Long id);
    void activateHotel(Long hotelId);

    HotelInfoDto getHotelInfoById(Long hotelId);
}
