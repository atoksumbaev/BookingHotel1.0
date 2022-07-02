package com.example.bookinghotel.services.Impl;

import com.example.bookinghotel.dao.BookingDao;
import com.example.bookinghotel.mappers.BookingMapper;
import com.example.bookinghotel.mappers.RoomMapper;
import com.example.bookinghotel.mappers.UserMapper;
import com.example.bookinghotel.models.dtos.BookHistoryDto;
import com.example.bookinghotel.models.dtos.BookingDto;
import com.example.bookinghotel.models.dtos.UserDto;
import com.example.bookinghotel.models.entities.Booking;

import com.example.bookinghotel.models.enums.EStatusBooking;
import com.example.bookinghotel.models.response.Message;
import com.example.bookinghotel.services.BookHistoryService;
import com.example.bookinghotel.services.BookingService;

import com.example.bookinghotel.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingDao bookingDao;
    @Autowired
    private UserService userService;
    @Autowired
    private BookHistoryService bookHistoryService;
    private RoomMapper roomMapper = RoomMapper.INSTANCE;
    private UserMapper userMapper = UserMapper.INSTANCE;

    private final BookingMapper bookingMapper = BookingMapper.INSTANCE;


    @Override
    public ResponseEntity<?> save(BookingDto bookingDto) {
        Booking booking = bookingMapper.toEntity(bookingDto);
        booking.setStatusBooking(EStatusBooking.ACTIVE);
        Booking bookingSaved = bookingDao.save(booking);
        return new ResponseEntity<>(bookingSaved, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> update(BookingDto bookingDto) {
        boolean isExists = bookingDao.existsById(bookingDto.getId());
        if (!isExists){
            return new ResponseEntity<>(Message.of("User not found"), HttpStatus.NOT_FOUND);
        }else {
            Booking booking = bookingMapper.toEntity(bookingDto);
            Booking updatedBooking = bookingDao.save(booking);
            return new ResponseEntity<>(updatedBooking, HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<?> delete(BookingDto bookingDto) {
        Booking booking = bookingMapper.toEntity(bookingDto);
        booking.setStatusBooking(EStatusBooking.INACTIVE);
        ResponseEntity<?> bookingDeleted = update(bookingMapper.toDto(booking));
        if(bookingDeleted.getStatusCode().equals(HttpStatus.OK)){
            return new ResponseEntity<>(bookingDeleted,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(Message.of("Booking not deleted"), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public BookingDto findById(Long id) {
        Booking booking = bookingDao.findById(id).orElse(null);
        if (booking != null) {
            return bookingMapper.toDto(booking);
        }
        return null;
    }


    @Override
    @Transactional
    public ResponseEntity<?> cancelBooking(BookingDto bookingDto, String comment, Long userId) {
        try {
            BookingDto booking = findById(bookingDto.getId());
            Booking entityBooking = bookingMapper.toEntity(booking);
            entityBooking.setStatusBooking(EStatusBooking.INACTIVE);
            ResponseEntity<?> canceledBooking = update(bookingMapper.toDto(entityBooking));

            BookHistoryDto bookHistory = new BookHistoryDto();
            bookHistory.setBooking(bookingDto);
            bookHistory.setChangeDate(LocalDate.now());
            bookHistory.setComment(comment);
            bookHistory.setRoom(roomMapper.toDto(entityBooking.getRoom()));
            bookHistory.setCheckInDate(entityBooking.getCheckInDate());
            bookHistory.setCheckOutDate(entityBooking.getCheckOutDate());
            bookHistory.setGuest(userMapper.toDto(entityBooking.getGuest()));
            bookHistory.setUser(userService.findById(userId));
            bookHistory.setStatusBooking(entityBooking.getStatusBooking());
            ResponseEntity<?> savedBookingHistory = bookHistoryService.save(bookHistory);

            return ResponseEntity.ok(canceledBooking);

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }

    }
}
