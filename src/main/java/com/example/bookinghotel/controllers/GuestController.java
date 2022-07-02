package com.example.bookinghotel.controllers;

import com.example.bookinghotel.models.dtos.ReviewDto;
import com.example.bookinghotel.services.HotelService;
import com.example.bookinghotel.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/guest")
public class GuestController {
    @Autowired
    private HotelService hotelService;
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/mainPage")
    public ResponseEntity<?> getMainPage(@RequestParam Long cityId){
        return hotelService.findAllByCityAndByRating(cityId);
    }

    @PostMapping("/addReview")
    public ResponseEntity<?> addReview(@RequestBody ReviewDto reviewDto){
        return reviewService.save(reviewDto);
    }

}
