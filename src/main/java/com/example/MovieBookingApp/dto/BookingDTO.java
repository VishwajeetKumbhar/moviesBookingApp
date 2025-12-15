package com.example.MovieBookingApp.dto;

import com.example.MovieBookingApp.entity.BookingSatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookingDTO {

    private Integer numberOfSeats;
    private LocalDateTime bookingTime;
    private Double price;
    private BookingSatus bookingStatus;
    private List<String> seatNumbers;
    private Long userId;
    private Long showId;

}
