package com.example.theatreoffice.repos;

import com.example.theatreoffice.models.Performance;
import com.example.theatreoffice.models.Theatre;
import org.springframework.data.repository.CrudRepository;

import com.example.theatreoffice.models.Schedule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepo extends CrudRepository<Schedule, Long> {
    List<Schedule> findByTheatre(Theatre theatre);
    List<Schedule> findByPerformance(Performance performance);
    List<Schedule> findByFreeSeatsBalconyGreaterThanEqual(int freeSeatsBalcony);
    List<Schedule> findByFreeSeatsGroundFloorGreaterThanEqual(int freeSeatsGroundFloor);
    List<Schedule> findByFreeSeatsMezzanineGreaterThanEqual(int freeSeatsMezzanine);
}
