package com.company.debpro.airbnb.service;

import com.company.debpro.airbnb.dto.RoomDto;
import com.company.debpro.airbnb.entity.Hotel;
import com.company.debpro.airbnb.entity.Room;
import com.company.debpro.airbnb.entity.User;
import com.company.debpro.airbnb.exception.ResourceNotFoundException;
import com.company.debpro.airbnb.exception.UnAuthorizedException;
import com.company.debpro.airbnb.repository.HotelRepository;
import com.company.debpro.airbnb.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService{

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public RoomDto createNewRoom(Long hotelId, RoomDto roomDto) {
        log.info("Creating a room in a hotel with id: {}",hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel with hotelid not found: "+hotelId));

        //check if he has access
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user is not the owner of the hotel");
        }

        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);  //jpa plugin used for autocomplete or code suggestion/generation

        //To do: create inventory as soon as room is created for the hotel
        if(hotel.getActive()){
            inventoryService.initializeRoomForAYear(room);
        }
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("Getting all the available rooms for a hotel with id : {}",hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found with id : "+hotelId));

        return hotel.getRooms().stream().map((element) -> modelMapper.map(element, RoomDto.class)).collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("Getting a room by its id : {}",roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("Room nit exists with id: "+roomId));

        return modelMapper.map(room,RoomDto.class);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Deleting a room by its id : {}",roomId);
        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(()-> new ResourceNotFoundException("Room nit exists with id: "+roomId));

        //check if he has access
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(room.getHotel().getOwner())){
            throw new UnAuthorizedException("This user is not the owner of the hotel");
        }

        //To do :  Delete all future inventories for this room
        inventoryService.deleteAllInventoriesOfRoom(room);

        //First delete all the inventories and then i can delete the room
        roomRepository.deleteById(roomId);
        return;
    }
}
