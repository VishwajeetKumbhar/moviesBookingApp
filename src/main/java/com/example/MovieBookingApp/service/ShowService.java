package com.example.MovieBookingApp.service;

import com.example.MovieBookingApp.dto.ShowDTO;
import com.example.MovieBookingApp.entity.Booking;
import com.example.MovieBookingApp.entity.Movie;
import com.example.MovieBookingApp.entity.Show;
import com.example.MovieBookingApp.entity.Theater;
import com.example.MovieBookingApp.repository.MovieRepository;
import com.example.MovieBookingApp.repository.ShowRepository;
import com.example.MovieBookingApp.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShowService {
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private TheaterRepository theaterRepository;

    public Show createShow(ShowDTO showDTO) {
        Movie movie = movieRepository.findById(showDTO.getMovieId()).orElseThrow(() -> new RuntimeException("Movie Not Found"));
        Theater theater = theaterRepository.findById(showDTO.getTheaterId()).orElseThrow(() -> new RuntimeException("Theater Not Found"));

        Show show = new Show();
        show.setShowTime(showDTO.getShowTime());
        show.setPrice(showDTO.getPrice());
        show.setMovie(movie);
        show.setTheater(theater);
        return showRepository.save(show);
    }

    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    public List<Show> getShowsByMovie(Long movieid) {
        Optional<List<Show>> showListBox = showRepository.findByMovieId( movieid);
        if (showListBox.isPresent()) {
            return showListBox.get();
        } else throw new RuntimeException("No Show Found for Movie Id " + movieid);

    }
    public List<Show> getShowsByTheater(Long theaterid) {
        Optional<List<Show>> showListBox = showRepository.findByTheaterId(theaterid);
        if (showListBox.isPresent()) {
            return showListBox.get();
        } else throw new RuntimeException("No Show Found for Theater Id " + theaterid);

    }

    public Show updateShow(Long id, ShowDTO showDTO) {
        Show show = showRepository.findById(id).orElseThrow(() -> new RuntimeException("Show Not Found for Id " + id));
        Movie movie = movieRepository.findById(showDTO.getMovieId()).orElseThrow(() -> new RuntimeException("Movie Not Found"));
        Theater theater = theaterRepository.findById(showDTO.getTheaterId()).orElseThrow(() -> new RuntimeException("Theater Not Found"));

        show.setShowTime(showDTO.getShowTime());
        show.setPrice(showDTO.getPrice());
        show.setMovie(movie);
        show.setTheater(theater);
        return showRepository.save(show);
    }

    public void deleteShow(Long id) {
        if (!showRepository.existsById(id)) {
            throw new RuntimeException("Show Not Found for Id " + id);
        }
        List<Booking> bookings = showRepository.findById(id).get().getBookings();
        if (!bookings.isEmpty()) {
            throw new RuntimeException("Can't Delete the show with existing Bookings");
        }
        showRepository.deleteById(id);
    }


}
