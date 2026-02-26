package com.company.debpro.airbnb.controller;

import com.company.debpro.airbnb.dto.RoomDto;
import com.company.debpro.airbnb.service.RoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/hotels/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomAdminController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomDto> createNewRoom(@PathVariable Long hotelId,  @RequestBody RoomDto roomDto){
        log.info("Room creation controller started");
        RoomDto roomcreated = roomService.createNewRoom(hotelId, roomDto );
        return new ResponseEntity<>(roomcreated, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RoomDto>> getAllRoomsByHotelId(@PathVariable Long hotelId){
        log.info("Get all rooms controller started");
        List<RoomDto> rooms = roomService.getAllRoomsInHotel(hotelId);
        return ResponseEntity.ok(rooms);
    }
    @GetMapping("/{roomId}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long hotelId, @PathVariable Long roomId){
        log.info("Get a room controller started");
        RoomDto room = roomService.getRoomById(roomId);
        return ResponseEntity.ok(room);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<Void> deleteARoomById(@PathVariable Long hotelId, @PathVariable Long roomId){
        log.info("Delete a room controller started");
        roomService.deleteRoomById(roomId);
        return ResponseEntity.noContent().build();
    }
}
