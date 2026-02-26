package com.company.debpro.airbnb.service;

import com.company.debpro.airbnb.dto.HotelDto;
import com.company.debpro.airbnb.dto.HotelPriceDto;
import com.company.debpro.airbnb.dto.HotelSearchRequest;
import com.company.debpro.airbnb.entity.Hotel;
import com.company.debpro.airbnb.entity.Inventory;
import com.company.debpro.airbnb.entity.Room;
import com.company.debpro.airbnb.repository.HotelMinPriceRepository;
import com.company.debpro.airbnb.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;


    @Override
    public void initializeRoomForAYear(Room room) {
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for(; !today.isAfter(endDate); today = today.plusDays(1)){
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .date(today)
                    .city(room.getHotel().getCity())
                    .bookedCount(0)
                    .reservedCount(0)
                    .totalCount(room.getTotalCount())
                    .surgeFactor(BigDecimal.ONE)
                    .price(room.getBasePrice())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }

    @Override
    public void deleteAllInventoriesOfRoom(Room room) {
        LocalDate today = LocalDate.now();
        inventoryRepository.deleteByRoom(room);
    }

    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchRequest hotelSearchRequest) {
        Pageable pageable = PageRequest.of(hotelSearchRequest.getPage(), hotelSearchRequest.getSize());
        long dateCount = ChronoUnit.DAYS.between(hotelSearchRequest.getStartDate(),hotelSearchRequest.getEndDate()) +1;



//        Page<Hotel> hotelPage1= inventoryRepository.findHotelsWithAvailableInventory(
//                hotelSearchRequest.getCity(),
//                hotelSearchRequest.getStartDate(),
//                hotelSearchRequest.getEndDate(),
//                hotelSearchRequest.getRoomsCount(),
//                dateCount,
//                pageable);
        //Buisness Logic:
        Page<HotelPriceDto> hotelPage2 = hotelMinPriceRepository.findHotelsWithAvailableInventory(
                hotelSearchRequest.getCity(),
                hotelSearchRequest.getStartDate(),
                hotelSearchRequest.getEndDate(),
                hotelSearchRequest.getRoomsCount(),
                dateCount,
                pageable
        );

        return hotelPage2;
    }
}
