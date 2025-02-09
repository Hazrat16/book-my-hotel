package com.hazratdev.HazratHotel.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "bookins")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Checkin data is required")
    private LocalDate checkInDate;

    @Future(message = "Checkout data is must be future date")
    private LocalDate checkOutDate;

    @Min(value = 1,message = "Number of adults must not be less that 1")
    private int numOfAdults;

    @Min(value = 0,message = "Number of Children must not be less that 0")
    private int numOfChildren;

    private int totalNumOfGuest;

    private String bookingConfirmationCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;


    public void calculateTotalNumOfGuest() {
        totalNumOfGuest = numOfAdults + numOfChildren;
    }

    public void setNumOfAdults(@Min(value = 1, message = "Number of adults must not be less that 1") int numOfAdults) {
        this.numOfAdults = numOfAdults;
        calculateTotalNumOfGuest();
    }

    public void setNumOfChildren(@Min(value = 0, message = "Number of Children must not be less that 0") int numOfChildren) {
        this.numOfChildren = numOfChildren;
        calculateTotalNumOfGuest();
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", checkInDate=" + checkInDate +
                ", checkOutDate=" + checkOutDate +
                ", numOfAdults=" + numOfAdults +
                ", numOfChildren=" + numOfChildren +
                ", totalNumOfGuest=" + totalNumOfGuest +
                ", bookingConfirmationCode='" + bookingConfirmationCode + '\'' +
                ", user=" + user +
                '}';
    }
}
