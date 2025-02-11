package com.hazratdev.HazratHotel.repo;

import com.hazratdev.HazratHotel.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room,Long> {

    @Query("select distinct r.roomType from Room r")
    List<String> findDistinctRoomTypes();

    @Query("SELECT r FROM Room r WHERE r.roomType LIKE %:roomType% AND r.id NOT IN ("
            + "SELECT bk.room.id FROM Booking bk "
            + "WHERE (bk.checkInDate <= :checkOutDate) AND (bk.checkOutDate >= :checkInDate))")
    List<Room> findAvailableRoomByDatesAndTypes(LocalDate checkInDate, LocalDate checkOutDate, String roomType);


    @Query("SELECT r FROM Room r WHERE r.id NOT IN (SELECT b.room.id FROM Booking b)")
    List<Room> getAvailableRooms();
}
