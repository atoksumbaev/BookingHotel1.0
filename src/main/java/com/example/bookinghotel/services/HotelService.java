package com.example.bookinghotel.services;

import com.example.bookinghotel.models.dtos.HotelDto;
import com.example.bookinghotel.models.enums.EBedType;import org.springframework.http.ResponseEntity;

import java.time.LocalDate;import java.util.Date;
import java.util.List;

public interface HotelService {

    ResponseEntity<?> save(HotelDto hotelDto);

    ResponseEntity<?> update(HotelDto hotelDto);

    ResponseEntity<?> delete(HotelDto hotelDto);

    ResponseEntity<?> findAllByCityAndByRating(Long cityId);

    ResponseEntity<?> filter(Long cityId, LocalDate checkInDate, LocalDate checkOutDate, int guestsAmount, EBedType bedType);

    List<HotelDto> findAll();

    void countCurrentScore();

    HotelDto findById(Long id);
}
