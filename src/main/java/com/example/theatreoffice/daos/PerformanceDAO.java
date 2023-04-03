package com.example.theatreoffice.daos;

import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.models.Performance;

import java.util.List;
import java.util.Optional;

public interface PerformanceDAO {
    List<Performance> getAllPerformances();
    Optional<Performance> getPerformanceById(long id);
    List<Performance> getPerformanceByTitle(String title);
    List<Performance> getPerformancesByGenre(String genre);
    List<Performance> getPerformancesOrderedByRating(boolean descendOrder);
    List<Performance> getPerformancesByTitleAndGenre(String title, String genre);
    List<Performance> getPerformancesByDirector(Participant director);
    Performance save(Performance performance);
    void delete(Performance performance);
}
