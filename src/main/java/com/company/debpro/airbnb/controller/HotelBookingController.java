package com.company.debpro.airbnb.controller;

import com.company.debpro.airbnb.dto.BookingDto;
import com.company.debpro.airbnb.dto.BookingRequestDto;
import com.company.debpro.airbnb.dto.GuestDto;
import com.company.debpro.airbnb.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookings")
public class HotelBookingController {
    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> initializeBooking(@RequestBody BookingRequestDto bookingRequestDto){
        return ResponseEntity.ok(bookingService.initializeBooking(bookingRequestDto));
    }

    @PostMapping("/{bookingId}/addGuests")
    public ResponseEntity<BookingDto> addGuests(@PathVariable Long bookingId, @RequestBody List<GuestDto> guestDtoList ){
        return ResponseEntity.ok(bookingService.addGuests(bookingId, guestDtoList));
    }
}
