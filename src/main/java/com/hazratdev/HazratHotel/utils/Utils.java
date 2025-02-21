package com.hazratdev.HazratHotel.utils;

import com.hazratdev.HazratHotel.dto.BookingDTO;
import com.hazratdev.HazratHotel.dto.RoomDTO;
import com.hazratdev.HazratHotel.dto.UserDTO;
import com.hazratdev.HazratHotel.entity.Booking;
import com.hazratdev.HazratHotel.entity.Room;
import com.hazratdev.HazratHotel.entity.User;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Utils {

    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int DEFAULT_CONFIRMATION_CODE_LENGTH = 6;

    /**
     * Generates a random alphanumeric confirmation code of specified length.
     * If length is less than or equal to 0, uses a default length of 6.
     */
    public static String generateRandomConfirmationCode(int length) {
        int codeLength = (length <= 0) ? DEFAULT_CONFIRMATION_CODE_LENGTH : length;
        StringBuilder stringBuilder = new StringBuilder(codeLength);
        for (int i = 0; i < codeLength; i++) {
            int randomIndex = SECURE_RANDOM.nextInt(ALPHANUMERIC_STRING.length());
            stringBuilder.append(ALPHANUMERIC_STRING.charAt(randomIndex));
        }
        return stringBuilder.toString();
    }

    /**
     * Maps a User entity to a UserDTO with basic fields.
     */
    public static UserDTO mapUserEntityToUserDTO(User user) {
        if (user == null) return null;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName()); // Consistent field usage
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    /**
     * Maps a Room entity to a RoomDTO with basic fields.
     */
    public static RoomDTO mapRoomEntityToRoomDTO(Room room) {
        if (room == null) return null;
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomType(room.getRoomType());
        roomDTO.setRoomPrice(room.getRoomPrice());
        roomDTO.setRoomPhotoUrl(room.getRoomPhotoUrl());
        roomDTO.setRoomDescription(room.getRoomDescription());
        return roomDTO;
    }

    /**
     * Maps a Booking entity to a BookingDTO with basic fields.
     */
    public static BookingDTO mapBookingEntityToBookingDTO(Booking booking) {
        if (booking == null) return null;
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setId(booking.getId());
        bookingDTO.setCheckInDate(booking.getCheckInDate());
        bookingDTO.setCheckOutDate(booking.getCheckOutDate());
        bookingDTO.setNumOfAdults(booking.getNumOfAdults());
        bookingDTO.setNumOfChildren(booking.getNumOfChildren());
        bookingDTO.setTotalNumOfGuest(booking.getTotalNumOfGuest());
        bookingDTO.setBookingConfirmationCode(booking.getBookingConfirmationCode());
        return bookingDTO;
    }

    /**
     * Maps a Room entity to a RoomDTO, including its bookings if available.
     */
    public static RoomDTO mapRoomEntityToRoomDTOPlusBookings(Room room) {
        if (room == null) return null;
        RoomDTO roomDTO = mapRoomEntityToRoomDTO(room);
        if (room.getBookings() != null && !room.getBookings().isEmpty()) {
            roomDTO.setBookings(room.getBookings().stream()
                    .map(Utils::mapBookingEntityToBookingDTO)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        } else {
            roomDTO.setBookings(Collections.emptyList()); // Avoid null in DTO
        }
        return roomDTO;
    }

    /**
     * Maps a Booking entity to a BookingDTO, optionally including user and room details.
     */
    public static BookingDTO mapBookingEntityToBookingDTOPlusBookedRooms(Booking booking, boolean mapUser) {
        if (booking == null) return null;
        BookingDTO bookingDTO = mapBookingEntityToBookingDTO(booking);
        if (mapUser && booking.getUser() != null) {
            bookingDTO.setUser(mapUserEntityToUserDTO(booking.getUser()));
        }
        if (booking.getRoom() != null) {
            bookingDTO.setRoom(mapRoomEntityToRoomDTO(booking.getRoom()));
        }
        return bookingDTO;
    }

    /**
     * Maps a User entity to a UserDTO, including bookings with room details if available.
     */
    public static UserDTO mapUserEntityToUserDTOPlusUserBookingsAndRoom(User user) {
        if (user == null) return null;
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setRole(user.getRole());

        if (user.getBookings() != null && !user.getBookings().isEmpty()) {
            userDTO.setBookings(user.getBookings().stream()
                    .map(booking -> mapBookingEntityToBookingDTOPlusBookedRooms(booking, false))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
        } else {
            userDTO.setBookings(Collections.emptyList()); // Avoid null in DTO
        }
        return userDTO;
    }

    /**
     * Maps a list of User entities to a list of UserDTOs.
     */
    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        if (userList == null) return Collections.emptyList();
        return userList.stream()
                .map(Utils::mapUserEntityToUserDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of Room entities to a list of RoomDTOs.
     */
    public static List<RoomDTO> mapRoomListEntityToRoomListDTO(List<Room> roomList) {
        if (roomList == null) return Collections.emptyList();
        return roomList.stream()
                .map(Utils::mapRoomEntityToRoomDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Maps a list of Booking entities to a list of BookingDTOs.
     */
    public static List<BookingDTO> mapBookingListEntityToBookingListDTO(List<Booking> bookingList) {
        if (bookingList == null) return Collections.emptyList();
        return bookingList.stream()
                .map(Utils::mapBookingEntityToBookingDTO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}