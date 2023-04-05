package com.example.theatreoffice.controllers;

import com.example.theatreoffice.daos.*;
import com.example.theatreoffice.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TheatreController {
	@Autowired
	private TheatreDAO theatreDAO;
	@Autowired
	private ScheduleDAO scheduleDAO;
	@Autowired
	private PerformanceDAO performanceDAO;
	@Autowired
	private ParticipantDAO participantDAO;
	@Autowired
	private ParticipanceDAO participanceDAO;

	private static class Filter {
		private String performanceTitleSearched;
		private String directorSearched;
		private String actorSearched;

		public Filter() {
			performanceTitleSearched = "";
			directorSearched = "";
			actorSearched = "";
		}


		public String getPerformanceTitleSearched() {
			return performanceTitleSearched;
		}

		public void setPerformanceTitleSearched(String performanceTitleSearched) {
			this.performanceTitleSearched = performanceTitleSearched;
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

	@GetMapping("/theatres")
	public String theatresMain(Model model) {
		List<Theatre> theatres = theatreDAO.getAllTheatres();
		Filter filter = new Filter();
		model.addAttribute("filter", filter);
		model.addAttribute("theatres", theatres);
		return "theatres-main";
	}

	@PostMapping("/theatres")
	public String theatresFilter(@ModelAttribute("filter") @RequestBody TheatreController.Filter filter, Model model) {
		String performanceTitleSearched = filter.getPerformanceTitleSearched();
		List<Performance> performances = new ArrayList<>();
		List<Theatre> theatres = new ArrayList<>();
		String directorSearched = filter.getDirectorSearched();
		String actorSearched = filter.getActorSearched();

		if (!performanceTitleSearched.isEmpty()) {
			performances = performanceDAO.getPerformanceByTitle(performanceTitleSearched);
			for (Performance performance : performances) {
				List<Schedule> schedules = scheduleDAO.getScheduleByPerformance(performance);
				for (Schedule schedule : schedules) {
					Theatre theatre = schedule.getTheatre();
					if (!theatres.contains(theatre)) {
						theatres.add(theatre);
					}
				}
			}
		}

		if (!directorSearched.isEmpty()) {
			String[] directorNames = directorSearched.split(" ");
			Optional<Participant> directorOpt = participantDAO.getParticipantByFirstNameAndLastName(directorNames[0], directorNames[1]);
			if (directorOpt.isPresent()) {
				theatres.clear();
				performances.clear();
				Participant director = participantDAO.getParticipantByFirstNameAndLastName(directorNames[0], directorNames[1]).orElseThrow();
				performances = performanceDAO.getPerformancesByDirector(director);

				for (Performance performance : performances) {
					List<Schedule> schedules = scheduleDAO.getScheduleByPerformance(performance);
					for (Schedule schedule : schedules) {
						Theatre theatre = schedule.getTheatre();
						if (!theatres.contains(theatre)) {
							theatres.add(theatre);
						}
					}
				}
			}
		} else if (!actorSearched.isEmpty()) {
			String[] actorNames = actorSearched.split(" ");
			Optional<Participant> actorOpt = participantDAO.getParticipantByFirstNameAndLastName(actorNames[0], actorNames[1]);
			if (actorOpt.isPresent()) {
				Participant actor = actorOpt.get();
				List<Participance> participances = participanceDAO.getParticipancesByParticipant(actor);
				performances.clear();
				theatres.clear();
				for (Participance participance : participances) {
					Performance performance = participance.getPerformance();
					List<Schedule> schedules = scheduleDAO.getScheduleByPerformance(performance);
					for (Schedule schedule : schedules) {
						Theatre theatre = schedule.getTheatre();
						if (!theatres.contains(theatre)) {
							theatres.add(theatre);
						}
					}
				}
			}
		}

		model.addAttribute("theatres", theatres);
		return "theatres-main";
	}

	@GetMapping("/theatres/add")
	public String theatresAdd(Model model) {
		return "theatre-add";
	}

	@PostMapping("/theatres/add")
	public String theatresAdd(@RequestParam String name, @RequestParam String seatsGroundFloor,
								  @RequestParam String seatsBalcony, @RequestParam String seatsMezzanine,
								  @RequestParam String building, @RequestParam String street,
								  @RequestParam String town, @RequestParam String country, Model model) {
		int buildingInt = Integer.parseInt(building);
		int seatsGroundFloorInt = Integer.parseInt(seatsGroundFloor);
		int seatsBalconyInt = Integer.parseInt(seatsBalcony);
		int seatsMezzanineInt = Integer.parseInt(seatsMezzanine);

		Theatre theatre = new Theatre(name, seatsGroundFloorInt, seatsBalconyInt, seatsMezzanineInt, buildingInt, street, town, country);
		theatreDAO.save(theatre);

		return "redirect:/theatres";
	}

	@GetMapping("/theatres/{id}")
	public String theatreDetails(@PathVariable(value = "id") long id, Model model) {
		Optional<Theatre> theatreOpt = theatreDAO.getTheatreById(id);
		if (theatreOpt.isEmpty()) {
			return "redirect:/theatres";
		}
		Theatre theatre = theatreOpt.get();

		List<Schedule> schedule = scheduleDAO.getScheduleByTheatre(theatre);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

		model.addAttribute("theatre", theatre);
		model.addAttribute("schedule", schedule);
		model.addAttribute("formatter", formatter);
		return "theatre-details";
	}

	@GetMapping("/theatres/{id}/edit")
	public String theatreEdit(@PathVariable(value = "id") long id, Model model) {
		Optional<Theatre> theatreOpt = theatreDAO.getTheatreById(id);
		if (theatreOpt.isEmpty()) {
			return "redirect:/theatres";
		}
		Theatre theatre = theatreOpt.get();

		model.addAttribute("theatre", theatre);
		return "theatre-edit";
	}

	@PostMapping("/theatres/{id}/edit")
	public String theatreUpdate(@PathVariable(value = "id") long id,
								@RequestParam String name, @RequestParam String seatsGroundFloor,
								@RequestParam String seatsBalcony, @RequestParam String seatsMezzanine,
								@RequestParam String building, @RequestParam String street,
								@RequestParam String town, @RequestParam String country, Model model) {
		Theatre theatre = theatreDAO.getTheatreById(id).orElseThrow();

		int buildingInt = Integer.parseInt(building);
		int seatsGroundFloorInt = Integer.parseInt(seatsGroundFloor);
		int seatsBalconyInt = Integer.parseInt(seatsBalcony);
		int seatsMezzanineInt = Integer.parseInt(seatsMezzanine);

		theatre.setBuilding(buildingInt);
		theatre.setStreet(street);
		theatre.setTown(town);
		theatre.setCountry(country);

		theatre.setName(name);
		theatre.setSeatsGroundFloor(seatsGroundFloorInt);
		theatre.setSeatsBalcony(seatsBalconyInt);
		theatre.setSeatsMezzanine(seatsMezzanineInt);
		theatreDAO.save(theatre);

		return "redirect:/theatres/{id}";
	}

	@PostMapping("/theatres/{id}/remove")
	public String theatreRemove(@PathVariable(value = "id") long id, Model model) {
		Theatre theatre = theatreDAO.getTheatreById(id).orElseThrow();
		theatreDAO.delete(theatre);

		return "redirect:/theatres";
	}
}
