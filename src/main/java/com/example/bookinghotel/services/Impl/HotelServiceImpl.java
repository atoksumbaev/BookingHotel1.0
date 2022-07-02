package com.example.bookinghotel.services.Impl;

import com.example.bookinghotel.dao.HotelDao;
import com.example.bookinghotel.mappers.CityMapper;
import com.example.bookinghotel.mappers.HotelMapper;
import com.example.bookinghotel.mappers.UserMapper;
import com.example.bookinghotel.models.dtos.CityDto;
import com.example.bookinghotel.models.dtos.HotelDto;
import com.example.bookinghotel.models.dtos.ReviewDto;
import com.example.bookinghotel.models.dtos.UserDto;
import com.example.bookinghotel.models.entities.Hotel;
import com.example.bookinghotel.models.response.Message;
import com.example.bookinghotel.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired private HotelDao hotelDao;
    @Autowired private BookingService bookingService;
    @Autowired private ReviewService reviewService;
    @Autowired private CityService cityService;
    @Autowired private UserService userService;
    private final HotelMapper hotelMapper = HotelMapper.INSTANCE;
    private final CityMapper cityMapper = CityMapper.INSTANCE;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    @Transactional
    public ResponseEntity<?> save(HotelDto hotelDto) {
        ResponseEntity<?> managerSaved = userService.save(hotelDto.getManager());

        hotelDto.setManager((UserDto) managerSaved.getBody());
        Hotel savedHotel = hotelDao.save(hotelMapper.toEntity(hotelDto));
        return new ResponseEntity<>(hotelMapper.toDto(savedHotel), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> update(HotelDto hotelDto) {
        boolean isExists = hotelDao.existsById(hotelDto.getId());
        if (!isExists){
            return new ResponseEntity<>(Message.of("Hotel not found"), HttpStatus.NOT_FOUND);
        }else {
            Hotel hotel = hotelMapper.toEntity(hotelDto);
            Hotel updatedHotel = hotelDao.save(hotel);
            return new ResponseEntity<>(updatedHotel, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> delete(HotelDto hotelDto) {
        Hotel hotel = hotelMapper.toEntity(hotelDto);
        hotel.setActive(false);
        ResponseEntity<?> deletedHotel = update(hotelMapper.toDto(hotel));
        if (deletedHotel.getStatusCode().equals(HttpStatus.OK)){
            return new ResponseEntity<>(deletedHotel, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(Message.of("Hotel not deleted"), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> findAllByCityAndByRating(Long cityId) {
        CityDto cityDto = cityService.findById(cityId);
        List<Hotel> hotels = hotelDao.findAllByCityAndCurrentScore(cityMapper.toEntity(cityDto));
        if(hotels.size() != 0) {
            return ResponseEntity.ok(hotelMapper.toDtoList(hotels));
        }
        return new ResponseEntity<>(Message.of("Отели по этому городу не найдены"), HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> filter(Long cityId, Date checkInDate, Date checkOutDate, int guestsAmount) {
        return null;
    }

    @Override
    public List<HotelDto> findAll() {
        return hotelMapper.toDtoList(hotelDao.findAll());
    }

    @Override
    public void countCurrentScore() {
        List<HotelDto> hotelDtos = findAll();
        hotelDtos.stream().forEach(x->{
            List<ReviewDto> reviews = reviewService.findAllByHotelAndActive(x);
            Double sum = reviews.stream().mapToDouble(ReviewDto::getScore).sum();
            Double currentScore = Math.round((sum / reviews.size())/10.0) * 10.0;

            // на случай если double округление не сработает
            String result = String.format("%.1f", currentScore);

            x.setCurrentScore(currentScore);
            update(x);
        });
    }

    @Override
    public HotelDto findById(Long id) {
        Hotel hotel = hotelDao.findById(id).orElse(null);
        if(hotel != null) return hotelMapper.toDto(hotel);
        return null;
    }
}
