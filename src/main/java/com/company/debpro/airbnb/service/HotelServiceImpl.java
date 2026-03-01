package com.company.debpro.airbnb.service;

import com.company.debpro.airbnb.dto.HotelDto;
import com.company.debpro.airbnb.dto.HotelInfoDto;
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
public class HotelServiceImpl implements HotelService{

    private final HotelRepository hotelRepository;   //@RequiredArgsConstructor here will inject the constructor as its final, else below code you need to add
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;

//    public HotelServiceImpl(HotelRepository hotelRepository) {
//        this.hotelRepository = hotelRepository;
//    }

    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Creating a new hotel with name : {}",hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto,Hotel.class);
        hotel.setActive(false);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);
        hotel = hotelRepository.save(hotel);
        log.info("Created a new hotel with ID : {}",hotelDto.getId());

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Getting a hotel by ID: {}",id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Hotel not found with id: " + id)));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user is not the owner of the hotel");
        }

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Updating a hotel by ID: {}",id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Hotel not found with id: " + id)));

        //check if he has access
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user is not the owner of the hotel");
        }


        modelMapper.map(hotelDto,hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        log.info("Updated the hotel by ID: {}",id);

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + id));

        //check if he has access
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user is not the owner of the hotel");
        }


        //delete the future inventories
        for(Room room : hotel.getRooms()){
            inventoryService.deleteAllInventoriesOfRoom(room);
            roomRepository.deleteById(room.getId());
        }

        hotelRepository.deleteById(id);

    }

    @Override
    public void activateHotel(Long hotelId) {
        log.info("Activating a hotel by ID: {}", hotelId);
        boolean exists = hotelRepository.existsById(hotelId);
        if (!exists) {
            throw new ResourceNotFoundException("Hotel not found with id " + hotelId);
        }
        ;
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        //check if he has access
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!user.equals(hotel.getOwner())){
            throw new UnAuthorizedException("This user is not the owner of the hotel");
        }

        hotel.setActive(true);
        //To do: Create inventory for this hotel
        for(Room room : hotel.getRooms()){
            inventoryService.initializeRoomForAYear(room);
        }
        return;
    }

    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        List<RoomDto> rooms = hotel.getRooms().stream().map((element) -> modelMapper.map(element, RoomDto.class)).toList();
        return new HotelInfoDto(modelMapper.map(hotel,HotelDto.class), rooms.stream().map((element) -> modelMapper.map(element, RoomDto.class)).toList());
    }
}
