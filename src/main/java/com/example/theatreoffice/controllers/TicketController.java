package com.example.theatreoffice.controllers;

import com.example.theatreoffice.models.Performance;
import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Ticket;
import com.example.theatreoffice.repos.PerformanceRepo;
import com.example.theatreoffice.repos.ScheduleRepo;
import com.example.theatreoffice.repos.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TicketController {
    @Autowired
    TicketRepo ticketRepo;
    @Autowired
    PerformanceRepo performanceRepo;
    @Autowired
    ScheduleRepo scheduleRepo;

    @GetMapping("/performances/{perfId}/{schedId}")
    public String performanceTickets(@PathVariable(value = "perfId") long perfId,
                                     @PathVariable(value = "schedId") long schedId,
                                     Model model) {
        if (!(performanceRepo.existsById(perfId))) {
            return "redirect:/performances";
        }
        if (!(scheduleRepo.existsById(schedId))) {
            return "redirect:/performances";
        }

        Optional<Performance> performance = performanceRepo.findById(perfId);
        ArrayList<Performance> res = new ArrayList<>();
        performance.ifPresent(res::add);

        Optional<Schedule> sch = scheduleRepo.findById(schedId);
        ArrayList<Schedule> schedule = new ArrayList<>();
        sch.ifPresent(schedule::add);

        List<Ticket> tickets = ticketRepo.findBySchedule(schedule.get(0));

        model.addAttribute("performance", res);
        model.addAttribute("schedule", schedule);
        model.addAttribute("tickets", tickets);
        return "tickets";
    }
}
