package com.example.bookinghotel.services;

import com.example.bookinghotel.models.dtos.RoomDto;
import com.example.bookinghotel.models.request.RoomSaveWithPrice;
import org.springframework.http.ResponseEntity;

public interface RoomService {

    RoomDto save(RoomDto roomDto);
    ResponseEntity<?> update(RoomDto roomDto);
    ResponseEntity<?> delete(RoomDto roomDto);

    ResponseEntity<?> saveRoomWithPrice(RoomSaveWithPrice roomSaveWithPrice);
}
