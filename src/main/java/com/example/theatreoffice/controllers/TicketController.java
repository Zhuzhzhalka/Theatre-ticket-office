package com.example.theatreoffice.controllers;

import com.example.theatreoffice.daos.PerformanceDAO;
import com.example.theatreoffice.daos.ScheduleDAO;
import com.example.theatreoffice.daos.TicketDAO;
import com.example.theatreoffice.models.Performance;
import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
public class TicketController {
    @Autowired
    private TicketDAO ticketDAO;
    @Autowired
    private PerformanceDAO performanceDAO;
    @Autowired
    private ScheduleDAO scheduleDAO;

    private static class ErrorBuy {
        private String errorString;

        public ErrorBuy() {
            errorString = "";
        }
        public ErrorBuy(String errorString) {
            this.errorString = errorString;
        }

        public String getErrorString() {
            return errorString;
        }

        public void setErrorString(String errorString) {
            this.errorString = errorString;
        }
    }

    @GetMapping("/performances/{perfId}/{schedId}")
    public String performanceTickets(@PathVariable(value = "perfId") long perfId,
                                     @PathVariable(value = "schedId") long schedId,
                                     Model model) {
        Optional<Performance> performanceOpt = performanceDAO.getPerformanceById(perfId);
        if (performanceOpt.isEmpty()) {
            return "redirect:/performances";
        }

        Optional<Schedule> scheduleOpt = scheduleDAO.getScheduleById(schedId);
        if (scheduleOpt.isEmpty()) {
            return "redirect:/performances";
        }

        Performance performance = performanceOpt.get();
        Schedule schedule = scheduleOpt.get();

        List<Ticket> tickets = ticketDAO.getTicketsBySchedule(schedule);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

        model.addAttribute("formatter", formatter);
        model.addAttribute("performance", performance);
        model.addAttribute("schedule", schedule);
        model.addAttribute("tickets", tickets);
        return "tickets";
    }

    @GetMapping("/performances/{perfId}/{schedId}/add")
    public String buyTicketForm(@PathVariable(value = "perfId") long perfId,
                                       @PathVariable(value = "schedId") long schedId,
                                       Model model) {
        Optional<Performance> performanceOpt = performanceDAO.getPerformanceById(perfId);
        if (performanceOpt.isEmpty()) {
            return "redirect:/performances";
        }

        Optional<Schedule> scheduleOpt = scheduleDAO.getScheduleById(schedId);
        if (scheduleOpt.isEmpty()) {
            return "redirect:/performances";
        }

        Performance performance = performanceOpt.get();
        Schedule schedule = scheduleOpt.get();

        ErrorBuy errorBuy = new ErrorBuy();

        model.addAttribute("performance", performance);
        model.addAttribute("schedule", schedule);
        model.addAttribute("errorBuy", errorBuy);
        return "ticket-add";
    }

    private String manageError(long perfId, long schedId, String errorString, String retTemplateString, Model model) {
        Performance performance = performanceDAO.getPerformanceById(perfId).orElseThrow();
        Schedule schedule = scheduleDAO.getScheduleById(schedId).orElseThrow();
        ErrorBuy errorBuy = new ErrorBuy(errorString);
        model.addAttribute("performance", performance);
        model.addAttribute("schedule", schedule);
        model.addAttribute("errorBuy", errorBuy);
        return retTemplateString;
    }

    @PostMapping("/performances/{perfId}/{schedId}/add")
    public String buyTicket(@PathVariable(value = "perfId") long perfId,
                                       @PathVariable(value = "schedId") long schedId,
                                       @RequestParam String personFirstName,
                                       @RequestParam String personLastName,
                                       @RequestParam String seatString,
                                       Model model) {
        Schedule schedule = scheduleDAO.getScheduleById(schedId).orElseThrow();
        List<Ticket> ticketsSched = ticketDAO.getTicketsBySchedule(schedule);

        if (personFirstName.isEmpty() || personLastName.isEmpty() || seatString.isEmpty()) {
            return manageError(perfId, schedId, "Provide personal info, man", "ticket-add", model);
        }

        int seat = Integer.parseInt(seatString);

        for (Ticket ticket : ticketsSched) {
            if (seat == ticket.getSeat()) {
                return manageError(perfId, schedId, "Seat is unavailable", "ticket-add", model);
            }
        }

        int seatsGroundFloor = schedule.getTheatre().getSeatsGroundFloor();
        int seatsBalcony = schedule.getTheatre().getSeatsBalcony();
        int seatsMezzanine = schedule.getTheatre().getSeatsMezzanine();

        if (seat <= seatsGroundFloor) {
            int newFreeSeats = schedule.getFreeSeatsGroundFloor() - 1;
            if (newFreeSeats < 0) {
                return manageError(perfId, schedId, "No free seats", "ticket-add", model);
            }
            schedule.setFreeSeatsGroundFloor(newFreeSeats);
        } else if (seat <= seatsGroundFloor + seatsBalcony) {
            int newFreeSeats = schedule.getFreeSeatsBalcony() - 1;
            if (newFreeSeats < 0) {
                return manageError(perfId, schedId, "No free seats", "ticket-add", model);
            }
            schedule.setFreeSeatsBalcony(newFreeSeats);
        } else if (seat <= seatsGroundFloor + seatsBalcony + seatsMezzanine) {
            int newFreeSeats = schedule.getFreeSeatsMezzanine() - 1;
            if (newFreeSeats < 0) {
                return manageError(perfId, schedId, "No free seats", "ticket-add", model);
            }
            schedule.setFreeSeatsMezzanine(newFreeSeats);
        }

        scheduleDAO.save(schedule);

        Ticket newTicket = new Ticket(personFirstName, personLastName, seat, 500, schedule);
        ticketDAO.save(newTicket);
        return "redirect:/performances/{perfId}/{schedId}";
    }

    @PostMapping("/performances/{perfId}/{schedId}/{ticketId}/remove")
    public String removeTicket(@PathVariable(value = "perfId") long perfId,
                               @PathVariable(value = "schedId") long schedId,
                               @PathVariable(value = "ticketId") long ticketId,
                               Model model) {
        Ticket ticket = ticketDAO.getTicketById(ticketId).orElseThrow();
        int seat = ticket.getSeat();
        Schedule schedule = scheduleDAO.getScheduleById(schedId).orElseThrow();

        int seatsGroundFloor = schedule.getTheatre().getSeatsGroundFloor();
        int seatsBalcony = schedule.getTheatre().getSeatsBalcony();
        int seatsMezzanine = schedule.getTheatre().getSeatsMezzanine();

        if (seat <= seatsGroundFloor) {
            int newFreeSeats = schedule.getFreeSeatsGroundFloor() + 1;
            schedule.setFreeSeatsGroundFloor(newFreeSeats);
        } else if (seat <= seatsGroundFloor + seatsBalcony) {
            int newFreeSeats = schedule.getFreeSeatsBalcony() + 1;
            schedule.setFreeSeatsBalcony(newFreeSeats);
        } else if (seat <= seatsGroundFloor + seatsBalcony + seatsMezzanine) {
            int newFreeSeats = schedule.getFreeSeatsMezzanine() + 1;
            schedule.setFreeSeatsMezzanine(newFreeSeats);
        }

        scheduleDAO.save(schedule);
        ticketDAO.delete(ticket);
        return "redirect:/performances/{perfId}/{schedId}";
    }
}
