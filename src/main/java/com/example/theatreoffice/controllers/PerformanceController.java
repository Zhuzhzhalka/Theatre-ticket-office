package com.example.theatreoffice.controllers;

import com.example.theatreoffice.daos.ParticipanceDAO;
import com.example.theatreoffice.daos.ParticipantDAO;
import com.example.theatreoffice.daos.PerformanceDAO;
import com.example.theatreoffice.daos.ScheduleDAO;
import com.example.theatreoffice.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PerformanceController {
    @Autowired
    private PerformanceDAO performanceDAO;
    @Autowired
    private ScheduleDAO scheduleDAO;
    @Autowired
    private ParticipanceDAO participanceDAO;
    @Autowired
    private ParticipantDAO participantDAO;

    @GetMapping("/performances")
    public String performancesMain(Model model) {
        List<Performance> performances = performanceDAO.getAllPerformances();
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
                                  @RequestParam String directorFirstName, @RequestParam String directorLastName,
                                  Model model) {
        LocalTime duration = LocalTime.parse(durationString);
        double rating = Double.parseDouble(ratingString);
        Optional<Participant> directorOpt = participantDAO.getParticipantByFirstNameAndLastName(directorFirstName, directorLastName);
        if (directorOpt.isEmpty()) {
            // TODO: implement new participant (director) addition to Participant table
            return "redirect:/performances";
        }
        Participant director = directorOpt.get();
        Performance performance = new Performance(title, duration, genre, rating, director);
        performanceDAO.save(performance);

        return "redirect:/performances";
    }

    @GetMapping("/performances/{id}")
    public String performanceDetails(@PathVariable(value = "id") long id, Model model) {
        Optional<Performance> performanceOpt = performanceDAO.getPerformanceById(id);
        if (performanceOpt.isEmpty()) {
            return "redirect:/performances";
        }
        Performance performance = performanceOpt.get();

        List<Schedule> schedule = scheduleDAO.getScheduleByPerformance(performance);
        List<Participance> participances = participanceDAO.getParticipancesByPerformance(performance);
        ArrayList<Participant> participants = new ArrayList<>();
        for (Participance participance : participances) {
            participants.add(participance.getParticipant());
        }

        model.addAttribute("performance", performance);
        model.addAttribute("schedule", schedule);
        model.addAttribute("participants", participants);
        return "performance-details";
    }

    @GetMapping("/performances/{id}/edit")
    public String performanceEdit(@PathVariable(value = "id") long id, Model model) {
        Optional<Performance> performanceOpt = performanceDAO.getPerformanceById(id);
        if (performanceOpt.isEmpty()) {
            return "redirect:/performances";
        }
        Performance performance = performanceOpt.get();

        model.addAttribute("performance", performance);
        return "performance-edit";
    }

    @PostMapping("/performances/{id}/edit")
    public String performanceUpdate(@PathVariable(value = "id") long id,
                                    @RequestParam String title, @RequestParam String durationString,
                                    @RequestParam String genre, @RequestParam double rating,
                                    @RequestParam Participant director_id, Model model) {
        Performance performance = performanceDAO.getPerformanceById(id).orElseThrow();
        LocalTime duration = LocalTime.parse(durationString);

        performance.setTitle(title);
        performance.setDuration(duration);
        performance.setGenre(genre);
        performance.setRating(rating);
        performance.setDirector(director_id);
        performanceDAO.save(performance);

        return "redirect:/performances/{id}";
    }

    @PostMapping("/performances/{id}/remove")
    public String performanceRemove(@PathVariable(value = "id") long id, Model model) {
        Performance performance = performanceDAO.getPerformanceById(id).orElseThrow();
        performanceDAO.delete(performance);

        return "redirect:/performances";
    }
}
