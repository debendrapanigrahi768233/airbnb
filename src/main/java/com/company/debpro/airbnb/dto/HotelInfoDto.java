package com.company.debpro.airbnb.dto;

import com.company.debpro.airbnb.entity.Room;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class HotelInfoDto {
    private HotelDto hotel;
    private List<RoomDto> rooms;
}
