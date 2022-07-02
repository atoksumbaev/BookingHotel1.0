package com.example.bookinghotel.dao;

import com.example.bookinghotel.models.entities.City;
import com.example.bookinghotel.models.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelDao extends JpaRepository<Hotel, Long> {

    @Query("select h from Hotel h where h.city = ?1 order by h.currentScore DESC")
    List<Hotel> findAllByCityAndCurrentScore(City city);
}
