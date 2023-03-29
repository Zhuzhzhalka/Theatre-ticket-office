package com.example.theatreoffice.daos;

import com.example.theatreoffice.daos.impl.ParticipantImplDAO;
import com.example.theatreoffice.daos.impl.PerformanceImplDAO;
import com.example.theatreoffice.daos.impl.ScheduleImplDAO;
import com.example.theatreoffice.daos.impl.TheatreImplDAO;
import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.models.Performance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PerformanceDAOTest {
    @TestConfiguration
    static class PerformanceDAOTestContextConfiguration {

        @Bean
        public PerformanceDAO performanceDAO() {
            return new PerformanceImplDAO();
        }

        @Bean
        public ScheduleDAO scheduleDAO() {
            return new ScheduleImplDAO();
        }

        @Bean
        public ParticipantDAO participantDAO() {
            return new ParticipantImplDAO();
        }
    }

    @Autowired
    private PerformanceDAO performanceDAO;

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Autowired
    private ParticipantDAO participantDAO;

    @Test
    public void testGetPerformanceByTitle() {
        LocalTime duration = LocalTime.parse("01:00:00");
        Optional<Participant> directorOpt = participantDAO.getParticipantById(1);
        Assertions.assertTrue(directorOpt.isPresent());
        Participant director = directorOpt.get();
        Performance performanceExpected = new Performance("Три богатыря", duration, "Мюзикл", 9.1, director);
        performanceDAO.save(performanceExpected);

        List<Performance> performancesGot = performanceDAO.getPerformanceByTitle("Три богатыря");
        Assertions.assertEquals(1, performancesGot.size());
        Performance performanceGot = performancesGot.get(0);
        Assertions.assertEquals(performanceExpected, performanceGot);

        performanceDAO.delete(performanceExpected);
    }

    @Test
    public void testGetPerformanceByGenres() {
        Collection<String> genres = new ArrayList<>();
        genres.add("Оперетта");
        genres.add("Опера");

        LocalTime duration = LocalTime.parse("01:00:00");
        Optional<Participant> directorOpt = participantDAO.getParticipantById(1);
        Assertions.assertTrue(directorOpt.isPresent());
        Participant director = directorOpt.get();
        Performance performance1 = new Performance("Три богатыря", duration, "Опера", 9.1, director);
        Performance performance2 = new Performance("ОК", duration, "Оперетта", 5.1, director);
        List<Performance> performancesExpected = new ArrayList<>();
        performancesExpected.add(performance1);
        performancesExpected.add(performance2);
        performanceDAO.save(performance1);
        performanceDAO.save(performance2);

        List<Performance> performancesGot = performanceDAO.getPerformancesByGenres(genres);
        Assertions.assertEquals(performancesExpected, performancesGot);

        performanceDAO.delete(performance1);
        performanceDAO.delete(performance2);
    }

    @Test
    public void TestGetPerformancesOrderedByRating() {
        List<Performance> performancesExpected = new ArrayList<>();
        performancesExpected.add(performanceDAO.getPerformanceById(3).orElseThrow());
        performancesExpected.add(performanceDAO.getPerformanceById(2).orElseThrow());
        performancesExpected.add(performanceDAO.getPerformanceById(1).orElseThrow());

        List<Performance> performancesGot = performanceDAO.getPerformancesOrderedByRating(true);
        Assertions.assertEquals(performancesExpected, performancesGot);
    }
}
