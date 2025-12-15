package com.example.MovieBookingApp.repository;

import com.example.MovieBookingApp.entity.Theater;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TheaterRepository extends JpaRepository<Theater, Long> {

    public Optional<List<Theater>> findByLocation(String location);
}
