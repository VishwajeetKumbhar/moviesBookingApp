package com.example.MovieBookingApp.service;

import com.example.MovieBookingApp.dto.TheaterDTO;
import com.example.MovieBookingApp.entity.Theater;
import com.example.MovieBookingApp.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class TheaterService {
    @Autowired
    private TheaterRepository theaterRepository;

    public Theater addtheater(@RequestBody TheaterDTO theaterDTO){
        Theater theater = new Theater();
        theater.setTheaterName(theaterDTO.getTheaterName());
        theater.setTheaterLocation(theaterDTO.getTheaterLocation());
        theater.setTheaterCapacity(theaterDTO.getTheaterCapacity());
        theater.setTheaterScreenType(theaterDTO.getTheaterScreenType());
        return theaterRepository.save(theater);
    }

    public List<Theater> getByLoaction(String loaction){
        Optional<List<Theater>> listofTheaterBox = theaterRepository.findByLocation(loaction);
        if(listofTheaterBox.isPresent()){
            return listofTheaterBox.get();
        }else throw new RuntimeException("No Theater Found Location " + loaction);
    }

    public Theater updateTheater(Long id, TheaterDTO theaterDTO) {
        Theater theater = theaterRepository.findById(id).orElseThrow(() -> new RuntimeException("Theater Not Found for the Id"+ id));
        theater.setTheaterName(theaterDTO.getTheaterName());
        theater.setTheaterLocation(theaterDTO.getTheaterLocation());
        theater.setTheaterCapacity(theaterDTO.getTheaterCapacity());
        theater.setTheaterScreenType(theaterDTO.getTheaterScreenType());
        return theaterRepository.save(theater);
    }

    public void deleteTheater(Long id) {
        theaterRepository.deleteById(id);
    }



}
