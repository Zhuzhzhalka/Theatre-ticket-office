package com.example.theatreoffice.daos;

import com.example.theatreoffice.daos.impl.ParticipanceImplDAO;
import com.example.theatreoffice.daos.impl.ParticipantImplDAO;
import com.example.theatreoffice.daos.impl.PerformanceImplDAO;
import com.example.theatreoffice.models.Participance;
import com.example.theatreoffice.models.Participant;
import com.example.theatreoffice.models.Performance;
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
public class ParticipanceDAOTest {
    @TestConfiguration
    static class ParticipanceDAOTestContextConfiguration {

        @Bean
        public PerformanceDAO performanceDAO() {
            return new PerformanceImplDAO();
        }

        @Bean
        public ParticipanceDAO participanceDAO() {
            return new ParticipanceImplDAO();
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

    @Autowired
    private ParticipanceDAO participanceDAO;

    @Test
    public void testGetParticipancesByPerformance() {
        Participance participance1 = new Participance(performanceDAO.getPerformanceById(3).orElseThrow(), participantDAO.getParticipantById(1).orElseThrow(), "актёр");
        Participance participance2 = new Participance(performanceDAO.getPerformanceById(3).orElseThrow(), participantDAO.getParticipantById(2).orElseThrow(), "актёр");
        participanceDAO.save(participance1);
        participanceDAO.save(participance2);
        List<Participance> participancesExpected = new ArrayList<>();
        participancesExpected.add(participance1);
        participancesExpected.add(participance2);

        List<Participance> participancesGot = participanceDAO.getParticipancesByPerformance(performanceDAO.getPerformanceById(3).orElseThrow());
        Assertions.assertEquals(participancesExpected, participancesGot);

        participanceDAO.delete(participance1);
        participanceDAO.delete(participance2);
    }

    @Test
    public void testGetParticipancesByParticipant() {
        Participance participance1 = new Participance(performanceDAO.getPerformanceById(2).orElseThrow(), participantDAO.getParticipantById(3).orElseThrow(), "актёр");
        Participance participance2 = new Participance(performanceDAO.getPerformanceById(1).orElseThrow(), participantDAO.getParticipantById(3).orElseThrow(), "актёр");
        participanceDAO.save(participance1);
        participanceDAO.save(participance2);
        List<Participance> participancesExpected = new ArrayList<>();
        participancesExpected.add(participance1);
        participancesExpected.add(participance2);

        List<Participance> participancesGot = participanceDAO.getParticipancesByParticipant(participantDAO.getParticipantById(3).orElseThrow());
        Assertions.assertEquals(participancesExpected, participancesGot);

        participanceDAO.delete(participance1);
        participanceDAO.delete(participance2);
    }

    @Test
    public void testRoleInPerformance() {
        String roleExpected = "певец";
        Performance performance = performanceDAO.getPerformanceById(2).orElseThrow();
        Participant participant = participantDAO.getParticipantById(3).orElseThrow();
        Participance participanceExpected = new Participance(performance, participant, roleExpected);
        participanceDAO.save(participanceExpected);

        String roleGot = participanceDAO.participantRoleInPerformance(participant, performance).orElseThrow();
        Assertions.assertEquals(roleExpected, roleGot);

        participanceDAO.delete(participanceExpected);
    }
}
