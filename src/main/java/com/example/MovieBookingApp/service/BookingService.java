package com.example.MovieBookingApp.service;

import com.example.MovieBookingApp.dto.BookingDTO;
import com.example.MovieBookingApp.entity.Booking;
import com.example.MovieBookingApp.entity.BookingSatus;
import com.example.MovieBookingApp.entity.Show;
import com.example.MovieBookingApp.entity.User;
import com.example.MovieBookingApp.repository.BookingRepository;
import com.example.MovieBookingApp.repository.ShowRepository;
import com.example.MovieBookingApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private UserRepository userRepository;

    public Booking createBooking(BookingDTO bookingDTO) {
        Show show = showRepository.findById(bookingDTO.getShowId()).orElseThrow(() -> new RuntimeException("Show Not Found for Id "));
        if (!isSeatsAvailable(show.getId(), bookingDTO.getNumberOfSeats())) {
            throw new RuntimeException("No Seats Available for the Show Id " + show.getId());
        }
        if (bookingDTO.getSeatNumbers().size() != bookingDTO.getNumberOfSeats()) {
            throw new RuntimeException("Number of Seats and Seat Numbers are not matching");
        }
        validateDuplicateSeats(show.getId(), bookingDTO.getSeatNumbers());

        User user = userRepository.findById(bookingDTO.getUserId()).orElseThrow(() -> new RuntimeException("User Not Found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setSeatNumbers(bookingDTO.getSeatNumbers());
        booking.setNumberOfSeats(bookingDTO.getNumberOfSeats());
        booking.setPrice(calculateTotalAmount(show.getPrice(), bookingDTO.getNumberOfSeats()));
        booking.setBookingTime(LocalDateTime.now());
        booking.setBookingStatus(BookingSatus.PENDING);

        return bookingRepository.save(booking);
    }

    public boolean isSeatsAvailable(Long showid, Integer numberOfSeats) {
        Show show = showRepository.findById(showid).orElseThrow(() -> new RuntimeException("Show Not Found"));
        int bookedSeats = show.getBookings().stream().filter(booking -> booking.getBookingStatus() != BookingSatus.CANCELLED)
                .mapToInt(Booking::getNumberOfSeats).sum();
        return show.getTheater().getTheaterCapacity() - bookedSeats >= numberOfSeats;
    }

    public void validateDuplicateSeats(Long showId, List<String> seatNumbers) {
        Show show = showRepository.findById(showId).orElseThrow(() -> new RuntimeException("Show Not Found"));
        Set<String> occupiedSeats = show.getBookings().stream().filter(b -> b.getBookingStatus() != BookingSatus.CANCELLED)
                .flatMap(b -> b.getSeatNumbers().stream()).collect(Collectors.toSet());

        List<String> duplicateSeats = seatNumbers.stream().filter(occupiedSeats::contains).collect(Collectors.toList());
        if (!duplicateSeats.isEmpty()) {
            throw new RuntimeException("Seats are already booked");
        }
    }

    public Double calculateTotalAmount(Double price, Integer numberOfSeats) {
        return price * numberOfSeats;
    }

    public List<Booking> getUserBookings(Long userid) {
        return bookingRepository.findByUserId(userid);
    }

    public List<Booking> getShowBookings(Long showid) {
        return bookingRepository.findByShowId(showid);
    }

    public Booking confirmBooking(Long bookingid) {
        Booking booking = bookingRepository.findById(bookingid).orElseThrow(() -> new RuntimeException("Booking not Found"));
        if (booking.getBookingStatus() != BookingSatus.PENDING) {
            throw new RuntimeException("Booking is not in pending state");
        }
        //Payment API PROCESS
        booking.setBookingStatus(BookingSatus.CONFIRMED);
        return bookingRepository.save(booking);
    }

    public Booking cancelmBooking(Long bookingid) {
        Booking booking = bookingRepository.findById(bookingid).orElseThrow(() -> new RuntimeException("Booking not Found"));
        validateCancellation(booking);
        booking.setBookingStatus(BookingSatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public void validateCancellation(Booking booking) {
        LocalDateTime showTime = booking.getShow().getShowTime();
        LocalDateTime deadlineTime = showTime.minusHours(2);
        if (LocalDateTime.now().isAfter(deadlineTime)) {
            throw new RuntimeException("Cannot cancle the booking");
        }
        if (booking.getBookingStatus() == BookingSatus.CANCELLED) {
            throw new RuntimeException("Booking Already been cancelled");
        }
    }






}
