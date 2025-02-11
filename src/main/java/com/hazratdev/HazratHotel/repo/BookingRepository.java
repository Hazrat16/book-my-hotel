package com.hazratdev.HazratHotel.repo;

import com.hazratdev.HazratHotel.entity.Booking;
import com.hazratdev.HazratHotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByRoomId(Long roomId);

    List<Booking> findByBookingConfirmationCode(String confirmationCode);

    List<Booking> findByUserId(Long userId);
}
