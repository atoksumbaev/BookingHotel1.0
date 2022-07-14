package com.example.bookinghotel.controllers;

import com.example.bookinghotel.models.dtos.*;

import com.example.bookinghotel.services.*;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/admin")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdminController {

    @Autowired BookHistoryService bookHistoryService;
    @Autowired BookingService bookingService;
    @Autowired CityService cityService;
    @Autowired HotelService hotelService;
    @Autowired PriceService priceService;
    @Autowired ReplyToReviewService replyToReviewService;
    @Autowired ReviewService reviewService;
    @Autowired RoomService roomService;
    @Autowired UserService userService;


    @PostMapping("/saveCity")
    public ResponseEntity<?> saveCity(@RequestBody CityDto cityDto){
        return cityService.save(cityDto);
    }
    @PutMapping("/updateCity")
    public ResponseEntity<?> updateCity(@RequestBody CityDto cityDto){
        return cityService.update(cityDto);
    }
    @DeleteMapping("/deleteCity")
    public ResponseEntity<?> deleteCity(@RequestBody CityDto cityDto){
        return cityService.delete(cityDto);
    }
    @GetMapping("/get/list")
    public ResponseEntity<?> findAll(){
        return cityService.findAll();
    }

    @PostMapping("/saveHotel")
    public ResponseEntity<?> saveHotel(@RequestBody HotelDto hotelDto){
        return hotelService.save(hotelDto);
    }
    @PutMapping("/updateHotel")
    public ResponseEntity<?> updateHotel(@RequestBody HotelDto hotelDto){
        return hotelService.update(hotelDto);
    }
    @DeleteMapping("/deleteHotel")
    public ResponseEntity<?> deleteHotel(@RequestBody HotelDto hotelDto){
        return hotelService.delete(hotelDto);
    }

    @PostMapping("/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto){
        return userService.save(userDto);
    }
    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UserDto userDto){
        return userService.update(userDto);
    }
    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody UserDto userDto){
        return userService.delete(userDto);
    }


}
