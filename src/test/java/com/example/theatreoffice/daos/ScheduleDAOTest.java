package com.example.theatreoffice.daos;

import com.example.theatreoffice.daos.impl.PerformanceImplDAO;
import com.example.theatreoffice.daos.impl.ScheduleImplDAO;
import com.example.theatreoffice.daos.impl.TheatreImplDAO;
import com.example.theatreoffice.models.Performance;
import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Theatre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ScheduleDAOTest {
    @TestConfiguration
    static class ScheduleDAOTestContextConfiguration {

        @Bean
        public ScheduleDAO scheduleDAO() {
            return new ScheduleImplDAO();
        }

        @Bean
        public TheatreDAO theatreDAO() {
            return new TheatreImplDAO();
        }

        @Bean
        public PerformanceDAO performanceDAO() {
            return new PerformanceImplDAO();
        }
    }

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Autowired
    private TheatreDAO theatreDAO;

    @Autowired
    private PerformanceDAO performanceDAO;

    @Test
    public void testGetScheduleByTheatre() {
        Theatre theatre = theatreDAO.getTheatreById(1).orElseThrow();

        List<Schedule> schedulesExpected = new ArrayList<>();
        schedulesExpected.add(scheduleDAO.getScheduleById(1).orElseThrow());
        schedulesExpected.add(scheduleDAO.getScheduleById(2).orElseThrow());

        List<Schedule> schedulesGot = scheduleDAO.getScheduleByTheatre(theatre);
        Assertions.assertEquals(schedulesExpected, schedulesGot);
    }

    @Test
    public void testGetScheduleByPerformance() {
        Performance performance = performanceDAO.getPerformanceById(1).orElseThrow();

        List<Schedule> schedulesExpected = new ArrayList<>();
        schedulesExpected.add(scheduleDAO.getScheduleById(1).orElseThrow());
        schedulesExpected.add(scheduleDAO.getScheduleById(2).orElseThrow());
        schedulesExpected.add(scheduleDAO.getScheduleById(3).orElseThrow());

        List<Schedule> schedulesGot = scheduleDAO.getScheduleByPerformance(performance);
        Assertions.assertEquals(schedulesExpected, schedulesGot);
    }

    @Test
    public void testGetScheduleById() {
        Performance performance = performanceDAO.getPerformanceById(2).orElseThrow();
        Theatre theatre = theatreDAO.getTheatreById(1).orElseThrow();
        LocalDateTime dateTime = LocalDateTime.parse("2020-10-13T20:00:00");
        Schedule scheduleExpected = new Schedule(performance, theatre, dateTime, 50, 20, 10, 500, 1000, 2000);
        scheduleDAO.save(scheduleExpected);

        Optional<Schedule> somethingGot = scheduleDAO.getScheduleById(scheduleExpected.getId());
        Assertions.assertTrue(somethingGot.isPresent());
        Schedule scheduleGot = somethingGot.get();
        Assertions.assertEquals(scheduleExpected, scheduleGot);

        scheduleDAO.delete(scheduleExpected);
    }
}
