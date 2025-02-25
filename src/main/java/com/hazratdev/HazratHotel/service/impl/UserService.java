package com.hazratdev.HazratHotel.service.impl;

import com.hazratdev.HazratHotel.dto.LoginRequest;
import com.hazratdev.HazratHotel.dto.Response;
import com.hazratdev.HazratHotel.dto.UserDTO;
import com.hazratdev.HazratHotel.entity.User;
import com.hazratdev.HazratHotel.exception.OurException;
import com.hazratdev.HazratHotel.repo.UserRepository;
import com.hazratdev.HazratHotel.service.IUserService;
import com.hazratdev.HazratHotel.utils.JWTUtils;
import com.hazratdev.HazratHotel.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils  jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response register(User user){
        Response response = new Response();
        try {
            if(user.getRole() == null || user.getRole().isEmpty()){
                user.setRole("USER");
            }
            if(userRepository.existsByEmail(user.getEmail())){
                throw new OurException(user.getEmail() + " is already registered");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);
            response.setStatusCode(200);
            response.setUser(userDTO);
        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During USer Registration " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            var user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()-> new OurException(loginRequest.getEmail() + " is not found"));
            var token = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(user.getRole());
            response.setExpirationTime("7days");
            response.setMessage("Successfully logged in");
        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());
        }catch (Exception e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During USer Login " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllUsers() {
       Response response = new Response();

       try{
           List<User> userList = userRepository.findAll();
           List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
           response.setStatusCode(200);
           response.setMessage("Successfully retrieved all users");
           response.setUserList(userDTOList);
       }catch (OurException e){
           response.setStatusCode(500);
           response.setMessage("Error Occurred During USer Retrieval " + e.getMessage());
       }
       return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {

        Response response = new Response();
        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new OurException(userId + " is not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(200);
            response.setMessage("Successfully retrieved user booking history");
            response.setUser(userDTO);
        }catch (OurException e){
            response.setStatusCode(500);
            response.setMessage("Error Occurred During USer Retrieval " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteUser(String userId) {
        Response response = new Response();

        try{
            userRepository.findById(Long.valueOf(userId)).orElseThrow(()-> new OurException(userId + " is not found"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(200);
            response.setMessage("Successfully deleted user");
        }catch (OurException e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {

        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(()->new OurException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setUser(userDTO);
            response.setMessage("Get user by id successfully");

        }catch (OurException e){
            response.setStatusCode(400);
            response.setMessage(e.getMessage());

        }


        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email).orElseThrow(()-> new OurException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(200);
            response.setUser(userDTO);
            response.setMessage("Get user by email successfully");
        }catch (OurException e){
            response.setStatusCode(500);
            response.setMessage(e.getMessage());

        }

        return response;
    }
}
