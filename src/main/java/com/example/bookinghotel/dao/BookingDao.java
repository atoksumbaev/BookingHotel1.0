package com.example.bookinghotel.dao;

import com.example.bookinghotel.models.entities.Booking;
import com.example.bookinghotel.models.entities.Room;import com.example.bookinghotel.models.enums.EStatusBooking;import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;import java.util.List;

@Repository
public interface BookingDao extends JpaRepository<Booking, Long> {

    List<Booking> findAllByRoomAndStatusBooking(Room room, EStatusBooking statusBooking);

}
