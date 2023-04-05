package com.example.theatreoffice.controllers;

import com.example.theatreoffice.daos.*;
import com.example.theatreoffice.models.*;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private TheatreDAO theatreDAO;

    private final String[] genresAvailable = {
            "Комедия",
            "Мюзикл",
            "Балет",
            "Драма",
            "Опера",
            "Спектакль"
    };

    private static class Filter {
        private String genreSelected;
        private String titleSearched;
        private String theatreNameSelected;
        private String ratingSortType;
        private String dateStart;
        private String dateEnd;
        private String directorSearched;
        private String actorSearched;

        public Filter() {
            this.genreSelected = "";
            this.titleSearched = "";
            this.theatreNameSelected = "";
            this.ratingSortType = "";
            this.dateStart = "";
            this.dateEnd = "";
            this.directorSearched = "";
            this.actorSearched = "";
        }

        public String getGenreSelected() {
            return genreSelected;
        }

        public void setGenreSelected(String genreSelected) {
            this.genreSelected = genreSelected;
        }

        public String getTitleSearched() {
            return titleSearched;
        }

        public void setTitleSearched(String titleSearched) {
            this.titleSearched = titleSearched;
        }

        public String getTheatreNameSelected() {
            return theatreNameSelected;
        }

        public void setTheatreNameSelected(String theatreNameSelected) {
            this.theatreNameSelected = theatreNameSelected;
        }

        public String getRatingSortType() {
            return ratingSortType;
        }

        public void setRatingSortType(String ratingSortType) {
            this.ratingSortType = ratingSortType;
        }

        public String getDateStart() {
            return dateStart;
        }

        public void setDateStart(String dateStart) {
            this.dateStart = dateStart;
        }

        public String getDateEnd() {
            return dateEnd;
        }

        public void setDateEnd(String dateEnd) {
            this.dateEnd = dateEnd;
        }

        public String getDirectorSearched() {
            return directorSearched;
        }

        public void setDirectorSearched(String directorSearched) {
            this.directorSearched = directorSearched;
        }

        public String getActorSearched() {
            return actorSearched;
        }

        public void setActorSearched(String actorSearched) {
            this.actorSearched = actorSearched;
        }

    }

    @GetMapping("/performances")
    public String performancesMain(Model model) {
        List<Performance> performances = performanceDAO.getAllPerformances();
        List<Theatre> theatres = theatreDAO.getAllTheatres();
        Filter filter = new Filter();
        model.addAttribute("performances", performances);
        model.addAttribute("genresAvailable", genresAvailable);
        model.addAttribute("filter", filter);
        model.addAttribute("theatres", theatres);
        return "performances-main";
    }

    @PostMapping("/performances")
    public String performancesFilter(@ModelAttribute("filter") @RequestBody Filter filter, Model model) {
        List<Performance> performances = new ArrayList<>();
        List<Theatre> theatres = theatreDAO.getAllTheatres();
        String titleSearched = filter.getTitleSearched();
        String genreSelected = filter.getGenreSelected();
        String theatreNameSelected = filter.getTheatreNameSelected();
        String ratingSortType = filter.getRatingSortType();
        String dateStartString = filter.getDateStart();
        String dateEndString = filter.getDateEnd();
        String directorSearched = filter.getDirectorSearched();
        String actorSearched = filter.getActorSearched();

        if (!titleSearched.isEmpty() && !genreSelected.isEmpty()) {
            performances = performanceDAO.getPerformancesByTitleAndGenre(titleSearched, genreSelected);
        } else if (!titleSearched.isEmpty()) {
            performances = performanceDAO.getPerformanceByTitle(titleSearched);
        } else if (!genreSelected.isEmpty()) {
            performances = performanceDAO.getPerformancesByGenre(genreSelected);
        } else if (!theatreNameSelected.isEmpty()) {
            List<Schedule> schedules = scheduleDAO.getScheduleByTheatre(theatreDAO.getTheatresByName(theatreNameSelected).get(0));
            for (Schedule schedule : schedules) {
                if (performances.contains(schedule.getPerformance())) {
                    continue;
                }
                performances.add(schedule.getPerformance());
            }
        } else {
            performances = performanceDAO.getAllPerformances();
        }

        if (!ratingSortType.isEmpty()) {
            boolean descendOrder = ratingSortType.equals("desc");
            if (performances.isEmpty()) {
                performances = performanceDAO.getPerformancesOrderedByRating(descendOrder);
            } else {
                performances.sort((p1, p2) -> {
                    if (p1.getRating().compareTo(p2.getRating()) == 0) {
                        return 0;
                    } else if (p1.getRating().compareTo(p2.getRating()) > 0) {
                        return -BooleanUtils.toInteger(descendOrder, 1, -1);
                    }
                    return BooleanUtils.toInteger(descendOrder, 1, -1);
                });
            }
        }

        if (!dateStartString.isEmpty() && !dateEndString.isEmpty()) {
            performances.clear();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
            LocalDateTime dateStart = LocalDateTime.parse(dateStartString, formatter);
            LocalDateTime dateEnd = LocalDateTime.parse(dateEndString, formatter);
            List<Schedule> schedules = scheduleDAO.getScheduleByDateRange(dateStart, dateEnd);
            for (Schedule schedule : schedules) {
                if (performances.contains(schedule.getPerformance())) {
                    continue;
                }
                performances.add(schedule.getPerformance());
            }
        }

        if (!directorSearched.isEmpty()) {
            String[] directorNames = directorSearched.split(" ");
            Optional<Participant> directorOpt = participantDAO.getParticipantByFirstNameAndLastName(directorNames[0], directorNames[1]);
            if (directorOpt.isPresent()) {
                performances.clear();
                performances = performanceDAO.getPerformancesByDirector(directorOpt.get());
            }
        } else if (!actorSearched.isEmpty()) {
            String[] actorNames = actorSearched.split(" ");
            Optional<Participant> actorOpt = participantDAO.getParticipantByFirstNameAndLastName(actorNames[0], actorNames[1]);
            if (actorOpt.isPresent()) {
                Participant actor = actorOpt.get();
                List<Participance> participances = participanceDAO.getParticipancesByParticipant(actor);
                performances.clear();
                for (Participance participance : participances) {
                    Performance performance = participance.getPerformance();
                    if (performances.contains(performance)) {
                        continue;
                    }
                    performances.add(performance);
                }
            }
        }

        model.addAttribute("performances", performances);
        model.addAttribute("genresAvailable", genresAvailable);
        model.addAttribute("filter", filter);
        model.addAttribute("theatres", theatres);
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
        Participant director;
        LocalTime duration = LocalTime.parse(durationString);
        BigDecimal rating = new BigDecimal(ratingString);

        Optional<Participant> directorOpt = participantDAO.getParticipantByFirstNameAndLastName(directorFirstName, directorLastName);
        if (directorOpt.isEmpty()) {
            director = new Participant(directorFirstName, directorLastName);
            participantDAO.save(director);
        } else {
            director = directorOpt.get();
        }

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

        model.addAttribute("formatter", formatter);
        model.addAttribute("performance", performance);
        model.addAttribute("schedule", schedule);
        model.addAttribute("participances", participances);
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
                                    @RequestParam String genre, @RequestParam String ratingString,
                                    @RequestParam String directorFirstName, @RequestParam String directorLastName,
                                    Model model) {
        Performance performance = performanceDAO.getPerformanceById(id).orElseThrow();
        LocalTime duration = LocalTime.parse(durationString);
        Participant director;
        BigDecimal rating = new BigDecimal(ratingString);

        Optional<Participant> directorOpt = participantDAO.getParticipantByFirstNameAndLastName(directorFirstName, directorLastName);
        if (directorOpt.isEmpty()) {
            director = new Participant(directorFirstName, directorLastName);
            participantDAO.save(director);
        } else {
            director = directorOpt.get();
        }

        performance.setTitle(title);
        performance.setDuration(duration);
        performance.setGenre(genre);
        performance.setRating(rating);
        performance.setDirector(director);
        performanceDAO.save(performance);

        return "redirect:/performances/{id}";
    }

    @PostMapping("/performances/{id}/remove")
    public String performanceRemove(@PathVariable(value = "id") long id, Model model) {
        Performance performance = performanceDAO.getPerformanceById(id).orElseThrow();
        performanceDAO.delete(performance);

        return "redirect:/performances";
    }

    @GetMapping("/performances/{perfId}/add_schedule")
    public String addPerformanceScheduleForm(@PathVariable(value = "perfId") long perfId, Model model) {
        Performance performance = performanceDAO.getPerformanceById(perfId).orElseThrow();

        model.addAttribute("performance", performance);
        return "schedule-add";
    }

    @PostMapping("/performances/{perfId}/add_schedule")
    public String addPerformanceSchedule(@PathVariable(value = "perfId") long perfId,
                                         @RequestParam String theatreName, @RequestParam String countryTheatre,
                                         @RequestParam String townTheatre, @RequestParam String dateString,
                                         @RequestParam int priceGroundFloor, @RequestParam int priceBalcony,
                                         @RequestParam int priceMezzanine, Model model) {
        Performance performance = performanceDAO.getPerformanceById(perfId).orElseThrow();

        Theatre theatre = theatreDAO.getTheatre(theatreName, townTheatre, countryTheatre).orElseThrow();
        LocalDateTime date = LocalDateTime.parse(dateString);
        int seatsGroundFloor = theatre.getSeatsGroundFloor();
        int seatsBalcony = theatre.getSeatsBalcony();
        int seatsMezzanine = theatre.getSeatsMezzanine();

        Schedule schedule = new Schedule(performance, theatre, date, seatsGroundFloor, seatsBalcony, seatsMezzanine,
                                        priceGroundFloor, priceBalcony, priceMezzanine);

        scheduleDAO.save(schedule);

        return "redirect:/performances/{perfId}";
    }
}
