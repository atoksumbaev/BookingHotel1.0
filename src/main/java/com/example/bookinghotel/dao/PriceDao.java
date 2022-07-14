package com.example.bookinghotel.dao;

import com.example.bookinghotel.models.entities.Price;
import com.example.bookinghotel.models.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PriceDao extends JpaRepository<Price, Long> {

    @Query("select p from Price p where p.room = ?1 and p.startDate <= ?2 and p.endDate >= ?2")
    Price findByRoom(Room room, LocalDate now);
}
