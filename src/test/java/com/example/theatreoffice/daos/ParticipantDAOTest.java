package com.example.theatreoffice.daos;

import com.example.theatreoffice.daos.impl.ParticipantImplDAO;
import com.example.theatreoffice.daos.impl.ScheduleImplDAO;
import com.example.theatreoffice.models.Participant;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ParticipantDAOTest {
    @TestConfiguration
    static class ParticipantDAOTestContextConfiguration {

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
    private ParticipantDAO participantDAO;

    @Test
    public void testGetParticipantByFirstNameAndLastName() {
        Participant participantExpected = participantDAO.getParticipantById(1).orElseThrow();

        Participant participantGot = participantDAO.getParticipantByFirstNameAndLastName("Чернов", "Василий").orElseThrow();
        Assertions.assertEquals(participantExpected, participantGot);
    }
}
