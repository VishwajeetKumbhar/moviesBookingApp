package com.example.MovieBookingApp.repository;
import com.example.MovieBookingApp.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long>{

    public Optional<List<Show>> findByMovieId(Long movieid);
    public Optional<List<Show>> findByTheaterId(Long theaterid);

}
