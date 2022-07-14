package com.example.bookinghotel.services;

import com.example.bookinghotel.models.dtos.RoomDto;
import com.example.bookinghotel.models.entities.Hotel;import com.example.bookinghotel.models.enums.EBedType;import com.example.bookinghotel.models.request.RoomSaveWithPrice;
import org.springframework.http.ResponseEntity;import java.util.List;

public interface RoomService {

    RoomDto save(RoomDto roomDto);
    ResponseEntity<?> update(RoomDto roomDto);
    ResponseEntity<?> delete(RoomDto roomDto);

    ResponseEntity<?> saveRoomWithPrice(RoomSaveWithPrice roomSaveWithPrice);

    List<RoomDto> findAllRoomsByHotel(Hotel hotel, EBedType bedType, int capacity);
}
