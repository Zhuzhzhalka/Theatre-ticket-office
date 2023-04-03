package com.example.theatreoffice.daos;

import com.example.theatreoffice.daos.impl.ParticipantImplDAO;
import com.example.theatreoffice.daos.impl.PerformanceImplDAO;
import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.models.Performance;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
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
        public ParticipantDAO participantDAO() {
            return new ParticipantImplDAO();
        }
    }

    @Autowired
    private PerformanceDAO performanceDAO;

    @Autowired
    private ParticipantDAO participantDAO;

    @Test
    public void testGetPerformanceByTitle() {
        LocalTime duration = LocalTime.parse("01:00:00");
        Optional<Participant> directorOpt = participantDAO.getParticipantById(1);
        Assertions.assertTrue(directorOpt.isPresent());
        Participant director = directorOpt.get();
        BigDecimal rating = new BigDecimal("9.1");
        Performance performanceExpected = new Performance("Три богатыря", duration, "Мюзикл", rating, director);
        performanceDAO.save(performanceExpected);

        List<Performance> performancesGot = performanceDAO.getPerformanceByTitle("Три богатыря");
        Assertions.assertEquals(1, performancesGot.size());
        Performance performanceGot = performancesGot.get(0);
        Assertions.assertEquals(performanceExpected, performanceGot);

        performanceDAO.delete(performanceExpected);
    }

    @Test
    public void testGetPerformanceByGenre() {
        String genre = "Опера";

        LocalTime duration = LocalTime.parse("01:00:00");
        Optional<Participant> directorOpt = participantDAO.getParticipantById(1);
        Assertions.assertTrue(directorOpt.isPresent());
        Participant director = directorOpt.get();
        BigDecimal rating = new BigDecimal("9.1");
        Performance performanceExpected = new Performance("Три богатыря", duration, "Опера", rating, director);
        performanceDAO.save(performanceExpected);

        List<Performance> performancesGot = performanceDAO.getPerformancesByGenre(genre);
        Assertions.assertEquals(1, performancesGot.size());
        Assertions.assertEquals(performanceExpected, performancesGot.get(0));

        performanceDAO.delete(performanceExpected);
    }

    @Test
    public void TestGetPerformancesOrderedByRating() {
        List<Performance> performancesExpected1 = new ArrayList<>();
        performancesExpected1.add(performanceDAO.getPerformanceById(3).orElseThrow());
        performancesExpected1.add(performanceDAO.getPerformanceById(2).orElseThrow());
        performancesExpected1.add(performanceDAO.getPerformanceById(1).orElseThrow());

        List<Performance> performancesGot1 = performanceDAO.getPerformancesOrderedByRating(true);
        Assertions.assertEquals(performancesExpected1, performancesGot1);

        List<Performance> performancesExpected2 = new ArrayList<>();
        performancesExpected2.add(performanceDAO.getPerformanceById(1).orElseThrow());
        performancesExpected2.add(performanceDAO.getPerformanceById(2).orElseThrow());
        performancesExpected2.add(performanceDAO.getPerformanceById(3).orElseThrow());

        List<Performance> performancesGot2 = performanceDAO.getPerformancesOrderedByRating(false);
        Assertions.assertEquals(performancesExpected2, performancesGot2);
    }

    @Test
    public void testGetPerformanceById() {
        LocalTime duration = LocalTime.parse("01:00:00");
        Optional<Participant> directorOpt = participantDAO.getParticipantById(1);
        Assertions.assertTrue(directorOpt.isPresent());
        Participant director = directorOpt.get();
        BigDecimal rating = new BigDecimal("9.1");
        Performance performanceExpected = new Performance("Три богатыря", duration, "Опера", rating, director);
        performanceDAO.save(performanceExpected);

        Optional<Performance> somethingGot = performanceDAO.getPerformanceById(performanceExpected.getId());
        Assertions.assertTrue(somethingGot.isPresent());
        Performance performanceGot = somethingGot.get();
        Assertions.assertEquals(performanceExpected, performanceGot);
        performanceDAO.delete(performanceExpected);
    }

    @Test
    public void testGetAllPerformances() {
        Performance performance1 = performanceDAO.getPerformanceById(1).orElseThrow();
        Performance performance2 = performanceDAO.getPerformanceById(2).orElseThrow();
        Performance performance3 = performanceDAO.getPerformanceById(3).orElseThrow();

        List<Performance> performancesExpected = new ArrayList<>();
        performancesExpected.add(performance1);
        performancesExpected.add(performance2);
        performancesExpected.add(performance3);

        List<Performance> performancesGot = performanceDAO.getAllPerformances();
        Assertions.assertEquals(performancesExpected, performancesGot);
    }
}
