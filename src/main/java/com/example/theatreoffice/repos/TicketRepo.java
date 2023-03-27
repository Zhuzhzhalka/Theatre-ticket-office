package com.example.theatreoffice.repos;

import com.example.theatreoffice.models.Schedule;
import org.springframework.data.repository.CrudRepository;

import com.example.theatreoffice.models.Ticket;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepo extends CrudRepository<Ticket, Long> {
    List<Ticket> findBySchedule(Schedule schedule);
}
