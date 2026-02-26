package com.company.debpro.airbnb.service;

import com.company.debpro.airbnb.dto.HotelDto;
import com.company.debpro.airbnb.dto.HotelPriceDto;
import com.company.debpro.airbnb.dto.HotelSearchRequest;
import com.company.debpro.airbnb.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void initializeRoomForAYear(Room room);
    void deleteAllInventoriesOfRoom(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
