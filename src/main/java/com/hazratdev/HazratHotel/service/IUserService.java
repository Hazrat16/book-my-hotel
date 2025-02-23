package com.hazratdev.HazratHotel.service;

import com.hazratdev.HazratHotel.dto.LoginRequest;
import com.hazratdev.HazratHotel.dto.Response;
import com.hazratdev.HazratHotel.entity.User;

public interface IUserService {

    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserBookingHistory(String userId);

    Response deleteUser(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String email);
}
