package com.example.bookinghotel.models.dtos;

import com.example.bookinghotel.models.entities.City;
import lombok.Data;

import java.util.List;


@Data
public class HotelDto {
    Long id;
    String name;
    String description;
    String address;
    byte star;
    String phone;
    Double currentScore;
    String email;
    CityDto city;
    boolean active;
    UserDto manager;
}
