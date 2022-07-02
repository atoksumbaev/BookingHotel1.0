package com.example.bookinghotel.services;

import com.example.bookinghotel.models.dtos.HotelDto;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;

public interface HotelService {

    ResponseEntity<?> save(HotelDto hotelDto);

    ResponseEntity<?> update(HotelDto hotelDto);

    ResponseEntity<?> delete(HotelDto hotelDto);

    ResponseEntity<?> findAllByCityAndByRating(Long cityId);

    ResponseEntity<?> filter(Long cityId, Date checkInDate, Date checkOutDate, int guestsAmount);

    List<HotelDto> findAll();

    void countCurrentScore();

    HotelDto findById(Long id);
}
