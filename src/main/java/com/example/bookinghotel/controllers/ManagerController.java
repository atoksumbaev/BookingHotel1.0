package com.example.bookinghotel.controllers;

import com.example.bookinghotel.models.dtos.*;
import com.example.bookinghotel.models.request.RoomSaveWithPrice;
import com.example.bookinghotel.services.*;
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
    @Autowired PriceService priceService;
    @Autowired ReviewService reviewService;
    @Autowired ReplyToReviewService replyToReviewService;

    @PutMapping("/updateHotel")
    public ResponseEntity<?> updateHotel(@RequestBody HotelDto hotelDto){
        return hotelService.update(hotelDto);
    }

    @PostMapping("/uploadImg")
    public ResponseEntity<?> uploadImageToHotel(@RequestParam MultipartFile file, @RequestParam Long hotelId, @RequestParam int orderNum){
        return fileService.uploadImageToHotel(file, hotelId, orderNum);
    }

    @PostMapping("/saveRoom/price")
    public ResponseEntity<?> saveRoomWithPrice(@RequestBody RoomSaveWithPrice roomSaveWithPrice){
        return roomService.saveRoomWithPrice(roomSaveWithPrice);
    }

    @PutMapping("/updateRoom")
    public ResponseEntity<?> updateRoom(@RequestBody RoomDto roomDto){
        return roomService.update(roomDto);
    }

    @DeleteMapping("/deleteRoom")
    public ResponseEntity<?> deleteRoom(@RequestBody RoomDto roomDto){
        return roomService.delete(roomDto);
    }

    @PutMapping("/updatePrice")
    public ResponseEntity<?> updatePrice(@RequestBody PriceDto priceDto){
        return priceService.update(priceDto);
    }

    @DeleteMapping("/deletePrice")
    public ResponseEntity<?> deletePrice(@RequestBody PriceDto priceDto){
        return priceService.delete(priceDto);
    }

    @DeleteMapping("/deleteReview")
    public ResponseEntity<?> deleteReview(@RequestBody ReviewDto reviewDto){
        return reviewService.delete(reviewDto);
    }
    @PostMapping("/saveReplyToReview")
    public ResponseEntity<?> saveReplyToReview(@RequestBody ReplyToReviewDto replyToReviewDto){
        return replyToReviewService.save(replyToReviewDto);
    }
    @DeleteMapping("/deleteReplyToReview")
    public ResponseEntity<?> deleteReplyToReview(@RequestBody ReplyToReviewDto replyToReviewDto){
        return replyToReviewService.delete(replyToReviewDto);
    }


}
