package com.example.bookinghotel.controllers;

import com.example.bookinghotel.models.dtos.HotelDto;
import com.example.bookinghotel.models.dtos.RoomDto;
import com.example.bookinghotel.models.request.RoomSaveWithPrice;
import com.example.bookinghotel.services.FileService;
import com.example.bookinghotel.services.HotelService;
import com.example.bookinghotel.services.RoomService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/manager")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagerController {

    @Autowired HotelService hotelService;
    @Autowired RoomService roomService;
    @Autowired FileService fileService;

    @PutMapping("/updateHotel")
    public ResponseEntity<?> updateHotel(@RequestBody HotelDto hotelDto){
        return hotelService.update(hotelDto);
    }
    @DeleteMapping("/deleteHotel")
    public ResponseEntity<?> deleteHotel(@RequestBody HotelDto hotelDto){
        return hotelService.delete(hotelDto);
    }
    @PostMapping("/saveRoom")
    public ResponseEntity<?> saveRoom(@RequestBody RoomSaveWithPrice roomDto){
        return roomService.saveRoomWithPrice(roomDto);
    }

    @PostMapping("/uploadImg")
    public ResponseEntity<?> uploadImageToHotel(@RequestParam MultipartFile file, @RequestParam Long hotelId, @RequestParam int orderNum){
        return fileService.uploadImageToHotel(file, hotelId, orderNum);
    }

}
