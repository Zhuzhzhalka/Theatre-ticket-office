package com.example.theatreoffice.daos.impl;

import com.example.theatreoffice.daos.ScheduleDAO;
import com.example.theatreoffice.models.Performance;
import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Theatre;
import com.example.theatreoffice.repos.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScheduleImplDAO implements ScheduleDAO {
    @Autowired
    private ScheduleRepo scheduleRepo;

    @Override
    public Optional<Schedule> getScheduleById(long id) {
        return scheduleRepo.findById(id);
    }

    @Override
    public List<Schedule> getScheduleByTheatre(Theatre theatre) {
        return scheduleRepo.findByTheatre(theatre);
    }

    @Override
    public List<Schedule> getScheduleByPerformance(Performance performance) {
        return scheduleRepo.findByPerformance(performance);
    }

    @Override
    public List<Schedule> getScheduleByDateRange(LocalDateTime dateStart, LocalDateTime dateEnd) {
        return scheduleRepo.findByDateTimeBetween(dateStart, dateEnd);
    }

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleRepo.save(schedule);
    }

    @Override
    public void delete(Schedule schedule) {
        scheduleRepo.delete(schedule);
    }
}
