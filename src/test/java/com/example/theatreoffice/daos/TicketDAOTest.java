package com.example.theatreoffice.daos;

import com.example.theatreoffice.daos.impl.ScheduleImplDAO;
import com.example.theatreoffice.daos.impl.TicketImplDAO;
import com.example.theatreoffice.models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TicketDAOTest {

    @TestConfiguration
    static class TicketDAOTestContextConfiguration {

        @Bean
        public TicketDAO ticketDAO() {
            return new TicketImplDAO();
        }

        @Bean
        public ScheduleDAO scheduleDAO() {
            return new ScheduleImplDAO();
        }
    }

    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private ScheduleDAO scheduleDAO;

    @Test
    public void testGetTicketsBySchedule() {
        Optional<Schedule> scheduleOpt = scheduleDAO.getScheduleById(1L);
        if (scheduleOpt.isEmpty()) {
            return;
        }
        Schedule schedule = scheduleOpt.get();

        List<Ticket> tickets = ticketDAO.getTicketsBySchedule(schedule);

        Assertions.assertNotNull(tickets);
    }

    @Test
    public void testGetTicketsByPerson() {
        Optional<Schedule> scheduleOpt = scheduleDAO.getScheduleById(1L);
        if (scheduleOpt.isEmpty()) {
            return;
        }
        Schedule schedule = scheduleOpt.get();
        Ticket ticketExpected = new Ticket("Смирнов", "Александр", 9, 500, schedule);
        ticketDAO.save(ticketExpected);
        List<Ticket> ticketsGot = ticketDAO.getTicketsByPerson("Смирнов", "Александр");

        Assertions.assertNotNull(ticketsGot);
        Assertions.assertEquals(1, ticketsGot.size());
        Assertions.assertEquals(ticketExpected.getPersonFirstName(), ticketsGot.get(0).getPersonFirstName());
        Assertions.assertEquals(ticketExpected.getPersonLastName(), ticketsGot.get(0).getPersonLastName());
        Assertions.assertEquals(ticketExpected, ticketsGot.get(0));
        ticketDAO.delete(ticketExpected);
    }

    @Test
    public void testGetTicketsBySeatAndSchedule() {
        Optional<Schedule> scheduleOpt = scheduleDAO.getScheduleById(1L);
        if (scheduleOpt.isEmpty()) {
            return;
        }
        Schedule schedule = scheduleOpt.get();
        Ticket ticketExpected = new Ticket("Смирнов", "Александр", 9, 500, schedule);
        ticketDAO.save(ticketExpected);
        Optional<Ticket> somethingGot = ticketDAO.getTicketBySeatAndSchedule(9, schedule);

        Assertions.assertTrue(somethingGot.isPresent());
        Ticket ticketGot = somethingGot.get();
        Assertions.assertEquals(ticketExpected, ticketGot);
        ticketDAO.delete(ticketExpected);
    }
}

