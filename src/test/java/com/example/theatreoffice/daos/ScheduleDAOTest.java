package com.example.theatreoffice.daos;

import com.example.theatreoffice.daos.impl.ScheduleImplDAO;
import com.example.theatreoffice.daos.impl.TheatreImplDAO;
import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Theatre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

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
    }

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Autowired
    private TheatreDAO theatreDAO;

    @Test
    public void testGetScheduleByTheatre() {
        Theatre theatre = theatreDAO.getTheatreById(1).orElseThrow();

        List<Schedule> schedulesExpected = new ArrayList<>();
        schedulesExpected.add(scheduleDAO.getScheduleById(1).orElseThrow());
        schedulesExpected.add(scheduleDAO.getScheduleById(2).orElseThrow());

        List<Schedule> schedulesGot = scheduleDAO.getScheduleByTheatre(theatre);
        Assertions.assertEquals(schedulesExpected, schedulesGot);
    }
}
