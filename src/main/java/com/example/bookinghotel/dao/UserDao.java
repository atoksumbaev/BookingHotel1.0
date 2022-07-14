package com.example.bookinghotel.dao;

import com.example.bookinghotel.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    User findByNameAndActiveTrue(String name);
}
