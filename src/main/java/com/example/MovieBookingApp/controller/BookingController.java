package com.example.MovieBookingApp.controller;

import com.example.MovieBookingApp.dto.BookingDTO;
import com.example.MovieBookingApp.entity.Booking;
import com.example.MovieBookingApp.entity.BookingSatus;
import com.example.MovieBookingApp.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/createbooking")
    public ResponseEntity<Booking> createBooking(@RequestBody BookingDTO bookingDTO) {
        return ResponseEntity.ok(bookingService.createBooking(bookingDTO));
    }

    @GetMapping("/getuserbookings/{id}")
    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getUserBookings(id));
    }

    @GetMapping("/getshowbookings/{id}")
    public ResponseEntity<List<Booking>> getShowBookings(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getShowBookings(id));
    }

    @PutMapping("{id}/confirm")
    public ResponseEntity<Booking> confirmBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.confirmBooking(id));
    }

    @PutMapping("{id}/cancel")
    public ResponseEntity<Booking> cancelmBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelmBooking(id));
    }

//    @GetMapping("/getbookingbystatus/{bookingStatus}")
//    public ResponseEntity<List<Booking>> getBookingByStatus(@PathVariable BookingSatus bookingStatus) {
//        return ResponseEntity.ok(bookingService.getBookingByStatus(bookingStatus));
//    }


}
