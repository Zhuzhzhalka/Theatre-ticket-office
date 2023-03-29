package com.example.theatreoffice.daos;

import com.example.theatreoffice.models.Performance;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PerformanceDAO {
    List<Performance> getAllPerformances();
    Optional<Performance> getPerformanceById(long id);
    List<Performance> getPerformanceByTitle(String title);
    List<Performance> getPerformancesByGenres(Collection<String> genres);
    List<Performance> getPerformancesOrderedByRating(boolean descendOrder);
    Performance save(Performance performance);
    void delete(Performance performance);
}
