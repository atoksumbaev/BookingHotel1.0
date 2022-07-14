package com.example.bookinghotel.controllers;

import com.example.bookinghotel.models.dtos.BookingDto;
import com.example.bookinghotel.models.dtos.ReviewDto;
import com.example.bookinghotel.models.enums.EBedType;
import com.example.bookinghotel.models.response.Message;
import com.example.bookinghotel.services.BookingService;
import com.example.bookinghotel.services.HotelService;
import com.example.bookinghotel.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/guest")
public class GuestController {
    @Autowired
    private HotelService hotelService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private BookingService bookingService;

    @GetMapping("/mainPage")
    public ResponseEntity<?> getMainPage(@RequestParam Long cityId){
        return hotelService.findAllByCityAndByRating(cityId);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(@RequestParam Long cityId,
                                    @RequestParam
                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
                                    @RequestParam
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
                                    @RequestParam int guestsAmount,
                                    @RequestParam EBedType bedType){
        return hotelService.filter(cityId, checkInDate, checkOutDate, guestsAmount, bedType);
    }

    @PostMapping("/saveBooking")
    public ResponseEntity<?> saveBooking(@RequestBody BookingDto bookingDto){
        return bookingService.save(bookingDto);
    }

    @PutMapping("/updateBooking")
    public ResponseEntity<?> updateBooking(@RequestBody BookingDto bookingDto){
        return bookingService.update(bookingDto);
    }

    @DeleteMapping("/deleteBooking")
    public ResponseEntity<?> deleteBooking(@RequestBody BookingDto bookingDto){
        return bookingService.delete(bookingDto);
    }

    @PostMapping("/saveReview")
    public ResponseEntity<?> saveReview(@RequestBody ReviewDto reviewDto){
        return reviewService.save(reviewDto);
    }

    @DeleteMapping("/deleteReview")
    public ResponseEntity<?> deleteReview(@RequestBody ReviewDto reviewDto){
        return reviewService.delete(reviewDto);
    }

    @GetMapping("/get/reviewsByHotel")
    public ResponseEntity<?> findAllByHotel(@RequestParam Long hotelId){
        List<ReviewDto> reviewDtos = reviewService.findAllByHotelAndActive(hotelId);
        if(reviewDtos.isEmpty()){
            return new ResponseEntity<>(Message.of("Отзывы по отелю отсутствуют"), HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(reviewDtos, HttpStatus.OK);
        }
    }

}
