package com.example.MovieBookingApp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieDTO {
    private String name;
    private String description;
    private Integer duration;
    private String genre;
    private String language;
    private LocalDate releaseDate;
}
