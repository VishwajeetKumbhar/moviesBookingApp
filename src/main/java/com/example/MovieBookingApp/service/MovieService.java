package com.example.MovieBookingApp.service;

import com.example.MovieBookingApp.dto.MovieDTO;
import com.example.MovieBookingApp.entity.Movie;
import com.example.MovieBookingApp.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public Movie addMovie(MovieDTO movieDTO) {
        Movie movie = new Movie();
        movie.setName(movieDTO.getName());
        movie.setDescription(movieDTO.getDescription());
        movie.setDuration(movieDTO.getDuration());
        movie.setGenre(movieDTO.getGenre());
        movie.setLanguage(movieDTO.getLanguage());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> getMoviesByGenre(String genre) {
        Optional<List<Movie>> listofMovieBox = movieRepository.findByGenre(genre);
        if (listofMovieBox.isPresent()) {
            return listofMovieBox.get();
        } else throw new RuntimeException("No Movie Found Genre " + genre);
    }

    public List<Movie> getMoviesByLanguage(String language) {
        Optional<List<Movie>> listofMovieBox = movieRepository.findByLanguage(language);
        if(listofMovieBox.isPresent()){
            return listofMovieBox.get();
        }else throw new RuntimeException("No Movie Found Language " + language);
    }

    public Movie getMovieByTitle(String title){
     Optional<Movie> movieBox = movieRepository.findByName(title);
        if(movieBox.isPresent()){
            return movieBox.get();
        }
        else throw new RuntimeException("No Movie Found Title " + title);
    }

    public Movie updateMovie(Long id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new RuntimeException("Movie Not Found"));
        movie.setName(movieDTO.getName());
        movie.setDescription(movieDTO.getDescription());
        movie.setDuration(movieDTO.getDuration());
        movie.setGenre(movieDTO.getGenre());
        movie.setLanguage(movieDTO.getLanguage());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }






}
