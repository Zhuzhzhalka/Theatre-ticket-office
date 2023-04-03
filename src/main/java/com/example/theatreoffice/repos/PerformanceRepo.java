package com.example.theatreoffice.repos;

import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.models.Performance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface PerformanceRepo extends CrudRepository<Performance, Long> {
    List<Performance> findByTitle(String title);
    List<Performance> findByDurationLessThanEqual(LocalTime localTime);
    List<Performance> findByGenre(String genre);
    List<Performance> findAllByOrderByRatingDesc();
    List<Performance> findAllByOrderByRatingAsc();
    List<Performance> findByTitleAndGenre(String title, String genre);
    List<Performance> findByDirector(Participant director);
}
