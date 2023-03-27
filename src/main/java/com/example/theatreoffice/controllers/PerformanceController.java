package com.example.theatreoffice.controllers;

import com.example.theatreoffice.models.*;
import com.example.theatreoffice.repos.ParticipanceRepo;
import com.example.theatreoffice.repos.PerformanceRepo;
import com.example.theatreoffice.repos.ScheduleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class PerformanceController {
    @Autowired
    PerformanceRepo performanceRepo;
    @Autowired
    ScheduleRepo scheduleRepo;
    @Autowired
    ParticipanceRepo participanceRepo;

    @GetMapping("/performances")
    public String performancesMain(Model model) {
        Iterable<Performance> performances = performanceRepo.findAll();
        model.addAttribute("performances", performances);
        return "performances-main";
    }

    @GetMapping("/performances/add")
    public String performancesAdd(Model model) {
        return "performance-add";
    }

    @PostMapping("/performances/add")
    public String performancesAdd(@RequestParam String title, @RequestParam String durationString,
                                  @RequestParam String genre, @RequestParam String ratingString,
                                  @RequestParam Participant director_id, Model model) {
        LocalTime duration = LocalTime.parse(durationString);
        double rating = Double.parseDouble(ratingString);
        Performance performance = new Performance(title, duration, genre, rating, director_id);
        performanceRepo.save(performance);

        return "redirect:/performances";
    }

    @GetMapping("/performances/{id}")
    public String performanceDetails(@PathVariable(value = "id") long id, Model model) {
        if (!(performanceRepo.existsById(id))) {
            return "redirect:/performances";
        }

        Optional<Performance> performance = performanceRepo.findById(id);
        ArrayList<Performance> res = new ArrayList<>();
        performance.ifPresent(res::add);

        List<Schedule> schedule = scheduleRepo.findByPerformance(res.get(0));
        List<Participance> participances = participanceRepo.findByPerformance(res.get(0));
        ArrayList<Participant> participants = new ArrayList<>();
        for (Participance participance : participances) {
            participants.add(participance.getParticipant());
        }

        model.addAttribute("performance", res);
        model.addAttribute("schedule", schedule);
        model.addAttribute("participants", participants);
        return "performance-details";
    }

    @GetMapping("/performances/{id}/edit")
    public String performanceEdit(@PathVariable(value = "id") long id, Model model) {
        if (!(performanceRepo.existsById(id))) {
            return "redirect:/performances";
        }

        Optional<Performance> performance = performanceRepo.findById(id);
        ArrayList<Performance> res = new ArrayList<>();
        performance.ifPresent(res::add);

        model.addAttribute("performance", res);
        return "performance-edit";
    }

    @PostMapping("/performances/{id}/edit")
    public String performanceUpdate(@PathVariable(value = "id") long id,
                                    @RequestParam String title, @RequestParam String durationString,
                                    @RequestParam String genre, @RequestParam double rating,
                                    @RequestParam Participant director_id, Model model) {
        Performance performance = performanceRepo.findById(id).orElseThrow();

        LocalTime duration = LocalTime.parse(durationString);

        performance.setTitle(title);
        performance.setDuration(duration);
        performance.setGenre(genre);
        performance.setRating(rating);
        performance.setDirector(director_id);
        performanceRepo.save(performance);

        return "redirect:/performances/{id}";
    }

    @PostMapping("/performances/{id}/remove")
    public String performanceRemove(@PathVariable(value = "id") long id, Model model) {
        Performance performance = performanceRepo.findById(id).orElseThrow();
        performanceRepo.delete(performance);

        return "redirect:/performances";
    }
}
