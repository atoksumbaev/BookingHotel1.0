package com.example.bookinghotel.services.Impl;

import com.example.bookinghotel.dao.RoomDao;
import com.example.bookinghotel.mappers.RoomMapper;
import com.example.bookinghotel.models.dtos.HotelDto;
import com.example.bookinghotel.models.dtos.PriceDto;
import com.example.bookinghotel.models.dtos.RoomDto;
import com.example.bookinghotel.models.entities.Hotel;import com.example.bookinghotel.models.entities.Room;
import com.example.bookinghotel.models.enums.EBedType;import com.example.bookinghotel.models.request.RoomSaveWithPrice;
import com.example.bookinghotel.models.response.Message;
import com.example.bookinghotel.services.HotelService;
import com.example.bookinghotel.services.PriceService;
import com.example.bookinghotel.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired private RoomDao roomDao;
    @Autowired private HotelService hotelService;
    @Autowired private PriceService priceService;

    private final RoomMapper roomMapper = RoomMapper.INSTANCE;

    @Override
    public RoomDto save(RoomDto roomDto) {
        Room room = roomMapper.toEntity(roomDto);
        room.setActive(true);
        Room saveRoom = roomDao.save(room);
        return roomMapper.toDto(saveRoom);
    }

    @Override
    public ResponseEntity<?> update(RoomDto roomDto) {
        boolean isExists = roomDao.existsById(roomDto.getId());
        if (!isExists){
            return new ResponseEntity<>(Message.of("Room not found"), HttpStatus.NOT_FOUND);
        }else {
            Room room = roomMapper.toEntity(roomDto);
            Room updatedRoom = roomDao.save(room);
            return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> delete(RoomDto roomDto) {
        Room room = roomMapper.toEntity(roomDto);
        room.setActive(false);
        ResponseEntity<?> deletedRoom = update(roomMapper.toDto(room));
        if (deletedRoom.getStatusCode().equals(HttpStatus.OK)){
            return new ResponseEntity<>(Message.of("Room is deleted"), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(Message.of("Room not deleted"), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> saveRoomWithPrice(RoomSaveWithPrice roomSaveWithPrice) {
        HotelDto hotelDto = hotelService.findById(roomSaveWithPrice.getHotelId());
        if(hotelDto == null) return new ResponseEntity<>(Message.of("Hotel object not found"), HttpStatus.NOT_ACCEPTABLE);
        RoomDto roomDto = new RoomDto();
        roomDto.setBedType(roomSaveWithPrice.getBedType());
        roomDto.setCapacity(roomSaveWithPrice.getCapacity());
        roomDto.setSquare(roomSaveWithPrice.getSquare());
        roomDto.setTypeOfView(roomSaveWithPrice.getTypeOfView());
        roomDto.setWifi(roomSaveWithPrice.isWifi());
        roomDto.setStatusBooking(roomSaveWithPrice.getStatusBooking());
        roomDto.setHotel(hotelDto);

        RoomDto savedRoom = save(roomDto);

        PriceDto priceDto = new PriceDto();
        priceDto.setActive(true);
        priceDto.setPrice(roomSaveWithPrice.getPrice());
        priceDto.setRoom(savedRoom);
        priceDto.setStartDate(roomSaveWithPrice.getStartDate());
        priceDto.setEndDate(roomSaveWithPrice.getEndDate());

        PriceDto saved = priceService.save(priceDto);
        return ResponseEntity.ok(Message.of("Room saved"));
    }


    @Override
    public List<RoomDto> findAllRoomsByHotel(Hotel hotel, EBedType bedType, int capacity) {
        List<Room> rooms = roomDao.findAllByActiveTrueAndHotelAndBedTypeAndCapacity(hotel, bedType, capacity);
        return roomMapper.toDtoList(rooms);
    }
}
