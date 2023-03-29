package com.example.theatreoffice.repos;

import org.springframework.data.repository.CrudRepository;

import com.example.theatreoffice.models.Performance;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface PerformanceRepo extends CrudRepository<Performance, Long> {
    List<Performance> findByTitle(String title);
    List<Performance> findByDurationLessThanEqual(LocalTime localTime);
    List<Performance> findByGenreIn(Collection<String> genres);
    List<Performance> findAllByOrderByRatingDesc();
    List<Performance> findAllByOrderByRatingAsc();
}
