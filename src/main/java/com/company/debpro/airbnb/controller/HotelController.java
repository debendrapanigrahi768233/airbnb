package com.company.debpro.airbnb.controller;

import com.company.debpro.airbnb.dto.HotelDto;
import com.company.debpro.airbnb.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelController {
    private final HotelService hotelService; //Here its just interface but SpringBoot will find the bean of the implementation of hotelService and going to inject it here.

    @PostMapping
    public ResponseEntity<HotelDto> createNewHotel (@RequestBody HotelDto hotelDto){
        log.info("Started controller call for creating a new hotel : {}",hotelDto.getName());
        HotelDto hotelDto1 = hotelService.createNewHotel(hotelDto);

        return new ResponseEntity<>(hotelDto1, HttpStatus.CREATED);
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDto> getHotelById (@PathVariable Long hotelId){
        log.info("Fetching a hotel with id : {}",hotelId);
        HotelDto hotelDto=hotelService.getHotelById(hotelId);
        return ResponseEntity.ok(hotelDto);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDto> updateHotelById (@PathVariable Long hotelId, @RequestBody HotelDto hotelDto){
        log.info("Started controller call for updating a new hotel with id: {}",hotelId);
        HotelDto hotelDto1 = hotelService.updateHotelById(hotelId, hotelDto);
        return ResponseEntity.ok(hotelDto1);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Void> updateHotelById (@PathVariable Long hotelId){
        log.info("Started controller call for deleting a new hotel with id: {}",hotelId);
        hotelService.deleteHotelById(hotelId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{hotelId}")
    public ResponseEntity<Void> activateHotelById (@PathVariable Long hotelId){
        log.info("Started controller call for activating a new hotel with id: {}",hotelId);
        hotelService.activateHotel(hotelId);
        return ResponseEntity.noContent().build();
    }

}
