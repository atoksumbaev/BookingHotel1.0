package com.example.bookinghotel.services;

import com.example.bookinghotel.models.dtos.BookingDto;
import com.example.bookinghotel.models.dtos.RoomDto;import org.springframework.http.ResponseEntity;import java.util.List;

public interface BookingService {

    ResponseEntity<?> save(BookingDto bookingDto);

    ResponseEntity<?> update(BookingDto bookingDto);

    ResponseEntity<?> delete(BookingDto bookingDto);

    BookingDto findById(Long id);

    ResponseEntity<?> cancelBooking(BookingDto bookingDto, String comment, Long userId);

    List<BookingDto> findAllByRoomAndActive(RoomDto roomDto);
}
