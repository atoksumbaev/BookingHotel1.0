package com.example.bookinghotel.services;

import com.example.bookinghotel.models.dtos.UserDto;
import com.example.bookinghotel.models.entities.User;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> save(UserDto userDto);
    ResponseEntity<?> update(UserDto userDto);
    ResponseEntity<?> delete(UserDto userDto);

    UserDto findById(Long id);

    User findByName(String name);

    //void checkAttempts(String password, String username);
}
