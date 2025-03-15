package com.hazratdev.HazratHotel.service.impl;

import com.hazratdev.HazratHotel.dto.Response;
import com.hazratdev.HazratHotel.dto.RoomDTO;
import com.hazratdev.HazratHotel.entity.Room;
import com.hazratdev.HazratHotel.exception.OurException;
import com.hazratdev.HazratHotel.repo.BookingRepository;
import com.hazratdev.HazratHotel.repo.RoomRepository;
import com.hazratdev.HazratHotel.service.AwsS3Service;
import com.hazratdev.HazratHotel.service.interfac.IRoomService;
import com.hazratdev.HazratHotel.utils.Utils;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class RoomService implements IRoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private AwsS3Service awsS3Service;


    @Override
    public Response addNewRoom(MultipartFile photo, String roomType, BigDecimal roomPrice, String description) {

        Response response = new Response();

        try {
            String imageUrl = awsS3Service.saveImageToS3(photo);
            Room room = new Room();
            room.setRoomType(roomType);
            room.setRoomPrice(roomPrice);
            room.setRoomPhotoUrl(imageUrl);
            room.setRoomDescription(description);
            Room savedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(savedRoom);
            response.setStatusCode(200);
            response.setMessage("Room created successfully");
            response.setRoom(roomDTO);

        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());

        }

        return response;
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public Response getAllRooms() {

        Response response = new Response();

        try {
            List<Room> rooms = roomRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(rooms);
            response.setStatusCode(200);
            response.setMessage("All rooms found");
            response.setRoomList(roomDTOList);
        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteRoom(Long roomId) {
        Response response = new Response();
        try {
            roomRepository .findById(roomId).orElseThrow(() -> new OurException("Room not found"));
            roomRepository.deleteById(roomId);
            response.setStatusCode(200);
            response.setMessage("Room deleted successfully");

        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Override
    public Response updateRoom(Long roomId, String description, String roomType, BigDecimal roomPrice, MultipartFile photo) {
        Response response = new Response();

        try {
            if (roomId == null) {
                throw new OurException("Room ID is required");
            }

            Room room = roomRepository.findById(roomId)
                    .orElseThrow(() -> new OurException("Room not found"));

            if (StringUtils.hasText(roomType)) {
                room.setRoomType(roomType);
            }

            if (roomPrice != null) {
                room.setRoomPrice(roomPrice);
            }

            if (StringUtils.hasText(description)) {
                room.setRoomDescription(description);
            }

            if (photo != null && !photo.isEmpty()) {
                String imageUrl = awsS3Service.saveImageToS3(photo);
                room.setRoomPhotoUrl(imageUrl);
            }

            Room updatedRoom = roomRepository.save(room);
            RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(updatedRoom);

            response.setStatusCode(200);
            response.setMessage("Room updated successfully");
            response.setRoom(roomDTO);

        } catch (OurException e) {
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("An unexpected error occurred");
        }

        return response;
    }


    @Override
    public Response getRoomById(Long roomId) {
        Response response = new Response();

        try {
           Room room = roomRepository .findById(roomId).orElseThrow(() -> new OurException("Room not found"));
           RoomDTO roomDTO = Utils.mapRoomEntityToRoomDTO(room);
           response.setStatusCode(200);
           response.setMessage("Room found");
           response.setRoom(roomDTO);
        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());

        }

        return response;
    }

    @Override
    public Response getAvailableRoomByDateAndType(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        Response response = new Response();

        try {
            List<Room> roomList = roomRepository.findAvailableRoomByDatesAndTypes(checkInDate, checkOutDate, roomType);
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(roomList);
            response.setStatusCode(200);
            response.setMessage("Available rooms found");
            response.setRoomList(roomDTOList);
        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());

        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAvailableRooms() {
        Response response = new Response();
        try {
            List<Room> room = roomRepository.getAvailableRooms();
            List<RoomDTO> roomDTOList = Utils.mapRoomListEntityToRoomListDTO(room);
            response.setStatusCode(200);
            response.setMessage("Available rooms found");
            response.setRoomList(roomDTOList);

        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
