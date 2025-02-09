package com.hazratdev.HazratHotel.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "rooms")
public class Room {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomType;

    private BigDecimal price;

    private String roomDescription;

    private String roomPhotoUrl;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();


    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", roomType='" + roomType + '\'' +
                ", price=" + price +
                ", roomDescription='" + roomDescription + '\'' +
                ", roomPhotoUrl='" + roomPhotoUrl + '\'' +
                ", bookings=" + bookings +
                '}';
    }
}
