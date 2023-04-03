package com.example.theatreoffice.daos.impl;

import com.example.theatreoffice.daos.TicketDAO;
import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Ticket;
import com.example.theatreoffice.repos.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketImplDAO implements TicketDAO {
    @Autowired
    private TicketRepo ticketRepo;

    @Override
    public Optional<Ticket> getTicketById(long id) {
        return ticketRepo.findById(id);
    }

    @Override
    public List<Ticket> getTicketsBySchedule(Schedule schedule) {
        return ticketRepo.findBySchedule(schedule);
    }

    @Override
    public List<Ticket> getTicketsByPerson(String firstName, String lastName) {
        return ticketRepo.findByPersonFirstNameAndPersonLastName(firstName, lastName);
    }

    @Override
    public Optional<Ticket> getTicketBySeatAndSchedule(int seat, Schedule schedule) {
        return ticketRepo.findBySeatAndSchedule(seat, schedule);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepo.save(ticket);
    }

    @Override
    public void delete(Ticket ticket) {
        ticketRepo.delete(ticket);
    }
}
