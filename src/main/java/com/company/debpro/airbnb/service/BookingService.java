package com.company.debpro.airbnb.service;

import com.company.debpro.airbnb.dto.BookingDto;
import com.company.debpro.airbnb.dto.BookingRequestDto;
import com.company.debpro.airbnb.dto.GuestDto;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface BookingService {
    BookingDto initializeBooking(BookingRequestDto bookingRequestDto);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
