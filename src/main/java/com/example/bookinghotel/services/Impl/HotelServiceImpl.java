package com.example.bookinghotel.services.Impl;

import com.example.bookinghotel.dao.HotelDao;
import com.example.bookinghotel.mappers.CityMapper;
import com.example.bookinghotel.mappers.HotelMapper;
import com.example.bookinghotel.mappers.RoomMapper;
import com.example.bookinghotel.mappers.UserMapper;
import com.example.bookinghotel.models.dtos.*;
import com.example.bookinghotel.models.entities.Hotel;
import com.example.bookinghotel.models.entities.Price;
import com.example.bookinghotel.models.enums.EBedType;
import com.example.bookinghotel.models.response.HotelFilterResponse;
import com.example.bookinghotel.models.response.Message;
import com.example.bookinghotel.models.response.RoomFilterResponse;
import com.example.bookinghotel.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class HotelServiceImpl implements HotelService {
    @Autowired private HotelDao hotelDao;
    @Autowired private BookingService bookingService;
    @Autowired private ReviewService reviewService;
    @Autowired private CityService cityService;
    @Autowired private UserService userService;
    @Autowired private RoomService roomService;
    @Autowired private PriceService priceService;
    private final HotelMapper hotelMapper = HotelMapper.INSTANCE;
    private final CityMapper cityMapper = CityMapper.INSTANCE;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final RoomMapper roomMapper = RoomMapper.INSTANCE;

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
    public ResponseEntity<?> filter(Long cityId, LocalDate checkInDate, LocalDate checkOutDate, int guestsAmount, EBedType bedType) {
        // получили все отели по городу и наличию нужного типа комнаты
        List<Hotel> hotels = hotelDao.findAllByCityAndBedType(cityId, bedType);
        List<HotelFilterResponse> filteredHotels = new ArrayList<>();
        List<Hotel> availableHotels = new ArrayList<>();
        hotels.stream().forEach(x->{
            // поличили все номера отеля по типу комнаты
             List<RoomDto> rooms = roomService.findAllRoomsByHotel(x, bedType, guestsAmount);
             List<RoomDto> availableRooms = new ArrayList<>();
             rooms.stream().forEach(room->{
                 List<BookingDto> bookings = bookingService.findAllByRoomAndActive(room);
                 if(bookings.isEmpty()){
                     availableRooms.add(room);
                 }else{
                     AtomicBoolean isBooked = new AtomicBoolean(false);
                     bookings.stream().forEach(booking->{
                         if(checkIsBooked(booking, checkInDate, checkOutDate)){
                             System.out.println("Room is booked");
                             isBooked.set(true);
                         }
                     });

                     if(isBooked.equals(false)){
                         availableRooms.add(room);
                     }
                 }
             });

             if(!availableRooms.isEmpty()){
                 availableHotels.add(x);
                 HotelFilterResponse response = formHotelResponse(x, availableRooms, checkInDate, checkOutDate);
                 filteredHotels.add(response);
             }
        });
        return new ResponseEntity<>(filteredHotels, HttpStatus.OK);
    }

    @Override
    public List<HotelDto> findAll() {
        return hotelMapper.toDtoList(hotelDao.findAll());
    }

    @Override
    public void countCurrentScore() {
        List<HotelDto> hotelDtos = findAll();
        hotelDtos.stream().forEach(x->{
            List<ReviewDto> reviews = reviewService.findAllByHotelAndActive(x.getId());
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


    private boolean checkIsBooked(BookingDto bookingDto, LocalDate startDate, LocalDate endDate){
        if(startDate.equals(bookingDto.getCheckInDate())
                || startDate.equals(bookingDto.getCheckOutDate())
                || (startDate.isAfter(bookingDto.getCheckInDate()) && startDate.isBefore(bookingDto.getCheckOutDate()))
                || endDate.equals(bookingDto.getCheckInDate())
                || endDate.equals(bookingDto.getCheckOutDate())
                || (endDate.isAfter(bookingDto.getCheckInDate()) && endDate.isBefore(bookingDto.getCheckOutDate()))
                || (startDate.isBefore(bookingDto.getCheckInDate()) && endDate.isAfter(bookingDto.getCheckOutDate()))
        ){
            return true;
        }else{
            return false;
        }
    }

    private HotelFilterResponse formHotelResponse(Hotel hotel, List<RoomDto> roomDtos, LocalDate checkIn, LocalDate checkOut){
        HotelFilterResponse hotelResponse = new HotelFilterResponse();
        hotelResponse.setId(hotel.getId());
        hotelResponse.setAddress(hotel.getAddress());
        hotelResponse.setCurrentScore(hotel.getCurrentScore());
        hotelResponse.setDescription(hotel.getDescription());
        hotelResponse.setEmail(hotel.getEmail());
        hotelResponse.setName(hotel.getName());
        hotelResponse.setPhone(hotel.getPhone());

        List<RoomFilterResponse> roomResponse = new ArrayList<>();
        roomDtos.stream().forEach(room-> {
            Price priceForCheckIn = priceService.findByRoom(roomMapper.toEntity(room), checkIn);
            Price priceForCheckOut = priceService.findByRoom(roomMapper.toEntity(room), checkOut);
            if(priceForCheckIn != null && priceForCheckOut != null) {
                if(priceForCheckIn.getPrice() == priceForCheckOut.getPrice()) {
                    long daysBetween = DAYS.between(checkIn, checkOut) + 1;
                    float totalSum = priceForCheckIn.getPrice() * daysBetween;

                    RoomFilterResponse roomFilterResponse = RoomFilterResponse.builder()
                            .bedType(room.getBedType())
                            .capacity(room.getCapacity())
                            .checkInDate(checkIn)
                            .checkOutDate(checkOut)
                            .id(room.getId())
                            .square(room.getSquare())
                            .typeOfView(room.getTypeOfView())
                            .wifi(room.isWifi())
                            .totalSum(totalSum)
                            .build();

                    roomResponse.add(roomFilterResponse);
                }else{
                    long daysBetween = DAYS.between(checkIn, priceForCheckIn.getEndDate()) + 1;
                    System.out.println("START: " + daysBetween);
                    float sumBeginning = daysBetween * priceForCheckIn.getPrice();
                    long daysBetween2 = DAYS.between(priceForCheckOut.getStartDate(), checkOut) + 1;
                    System.out.println("START: " + daysBetween2);
                    float sumEnding = daysBetween2 * priceForCheckOut.getPrice();
                    float totalSum = sumBeginning + sumEnding;

                    RoomFilterResponse roomFilterResponse = RoomFilterResponse.builder()
                            .bedType(room.getBedType())
                            .capacity(room.getCapacity())
                            .checkInDate(checkIn)
                            .checkOutDate(checkOut)
                            .id(room.getId())
                            .square(room.getSquare())
                            .typeOfView(room.getTypeOfView())
                            .wifi(room.isWifi())
                            .totalSum(totalSum)
                            .build();

                    roomResponse.add(roomFilterResponse);
                }
            }
        });

        hotelResponse.setAvailableRooms(roomResponse);
        return hotelResponse;
    }
}
