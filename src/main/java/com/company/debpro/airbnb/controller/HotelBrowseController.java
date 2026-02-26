package com.company.debpro.airbnb.controller;

import com.company.debpro.airbnb.dto.HotelDto;
import com.company.debpro.airbnb.dto.HotelInfoDto;
import com.company.debpro.airbnb.dto.HotelPriceDto;
import com.company.debpro.airbnb.dto.HotelSearchRequest;
import com.company.debpro.airbnb.repository.HotelRepository;
import com.company.debpro.airbnb.service.HotelService;
import com.company.debpro.airbnb.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {
    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest){
        Page<HotelPriceDto> page =inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }

}
