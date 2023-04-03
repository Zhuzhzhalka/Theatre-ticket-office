package com.example.theatreoffice.daos;

import com.example.theatreoffice.models.Performance;
import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Theatre;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleDAO {
    Optional<Schedule> getScheduleById(long id);
    List<Schedule> getScheduleByTheatre(Theatre theatre);
    List<Schedule> getScheduleByPerformance(Performance performance);
    List<Schedule> getScheduleByDateRange(LocalDateTime dateStart, LocalDateTime dateEnd);
    Schedule save(Schedule schedule);
    void delete(Schedule schedule);
}
