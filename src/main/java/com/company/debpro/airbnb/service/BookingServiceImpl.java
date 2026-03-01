package com.company.debpro.airbnb.service;

import com.company.debpro.airbnb.dto.BookingDto;
import com.company.debpro.airbnb.dto.BookingRequestDto;
import com.company.debpro.airbnb.dto.GuestDto;
import com.company.debpro.airbnb.entity.*;
import com.company.debpro.airbnb.entity.enums.BookingStatus;
import com.company.debpro.airbnb.exception.ResourceNotFoundException;
import com.company.debpro.airbnb.exception.UnAuthorizedException;
import com.company.debpro.airbnb.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final GuestRepository guestRepository;

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequestDto bookingRequestDto) {
        log.info("Initializing booking..");
        Hotel hotel = hotelRepository.findById(bookingRequestDto.getHotelId()).orElseThrow(()-> new ResourceNotFoundException("Hotel with requested id is not found: "));
        Room room = roomRepository.findById(bookingRequestDto.getRoomId()).orElseThrow(()-> new ResourceNotFoundException("Room with requested id is not found: "));
        List<Inventory> inventoryList = inventoryRepository.findAndLockAvailableInventory(
                room.getId(),
                bookingRequestDto.getCheckInDate(),
                bookingRequestDto.getCheckOutDate(),
                bookingRequestDto.getRoomsCount()
        );
        long daysCount = ChronoUnit.DAYS.between(bookingRequestDto.getCheckInDate(), bookingRequestDto.getCheckOutDate())+1;
        if(inventoryList.size() != daysCount){
            throw new IllegalStateException("Room is not available");
        }

        //Add the count of reserveCount for the inventories
        for(Inventory inventory: inventoryList){
            inventory.setReservedCount(inventory.getReservedCount()+bookingRequestDto.getRoomsCount());
        }

        //save all inventories
        inventoryRepository.saveAll(inventoryList);



        //To do: Calculate the dynamic price for all the rooms and the price based on date

        //create the booking
        Booking booking = Booking.builder()
                .hotel(hotel)
                .room(room)
                .roomsCount(bookingRequestDto.getRoomsCount())
                .checkInDate(bookingRequestDto.getCheckInDate())
                .checkOutDate(bookingRequestDto.getCheckOutDate())
                .bookingStatus(BookingStatus.RESERVED)
                .user(getCurrentUser())
                .amount(BigDecimal.TEN)
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding guest information for the booking");
        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not found with id: "+bookingId));

        User user = getCurrentUser();
        if(!user.equals(booking.getUser())){
            throw new UnAuthorizedException("Booking not belongs to the user with id : "+user.getId());
        }

        if(isBookingExpired(booking)){
            throw new IllegalStateException("Booking has been expired");
        }

        if(booking.getBookingStatus() != BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not under the reserved state , cannot add guests");
        }
        
        for(GuestDto guestDto: guestDtoList){
            Guest guest=modelMapper.map(guestDto, Guest.class);
            guest.setUser(user);
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    public boolean isBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser(){

        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
