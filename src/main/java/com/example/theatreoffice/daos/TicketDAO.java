package com.example.theatreoffice.daos;

import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketDAO {
    List<Ticket> getTicketsBySchedule(Schedule schedule);
    List<Ticket> getTicketsByPerson(String firstName, String lastName);
    Optional<Ticket> getTicketBySeatAndSchedule(int seat, Schedule schedule);
    Ticket save(Ticket ticket);
    void delete(Ticket ticket);
}
