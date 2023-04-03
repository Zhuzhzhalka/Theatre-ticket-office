package com.example.theatreoffice.daos.impl;

import com.example.theatreoffice.daos.PerformanceDAO;
import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.models.Performance;
import com.example.theatreoffice.repos.PerformanceRepo;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceImplDAO implements PerformanceDAO {
    @Autowired
    private PerformanceRepo performanceRepo;

    @Override
    public List<Performance> getAllPerformances() {
        return IterableUtils.toList(performanceRepo.findAll());
    }

    @Override
    public Optional<Performance> getPerformanceById(long id) {
        return performanceRepo.findById(id);
    }

    @Override
    public List<Performance> getPerformanceByTitle(String title) {
        return performanceRepo.findByTitle(title);
    }

    @Override
    public List<Performance> getPerformancesByGenre(String genre) {
        return performanceRepo.findByGenre(genre);
    }

    @Override
    public List<Performance> getPerformancesOrderedByRating(boolean descendOrder) {
        if (descendOrder) {
            return performanceRepo.findAllByOrderByRatingDesc();
        }
        return performanceRepo.findAllByOrderByRatingAsc();
    }

    @Override
    public List<Performance> getPerformancesByTitleAndGenre(String title, String genre) {
        return performanceRepo.findByTitleAndGenre(title, genre);
    }

    @Override
    public List<Performance> getPerformancesByDirector(Participant director) {
        return performanceRepo.findByDirector(director);
    }

    @Override
    public Performance save(Performance performance) {
        return performanceRepo.save(performance);
    }

    @Override
    public void delete(Performance performance) {
        performanceRepo.delete(performance);
    }
}
