package com.example.bookinghotel.services;

import com.example.bookinghotel.models.dtos.PriceDto;
import com.example.bookinghotel.models.entities.Price;
import com.example.bookinghotel.models.entities.Room;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface PriceService {

    PriceDto save(PriceDto priceDto);
    ResponseEntity<?> update(PriceDto priceDto);
    ResponseEntity<?> delete(PriceDto priceDto);

    Price findByRoom(Room room, LocalDate date);
}
