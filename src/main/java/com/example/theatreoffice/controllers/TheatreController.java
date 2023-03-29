package com.example.theatreoffice.controllers;

import com.example.theatreoffice.daos.ScheduleDAO;
import com.example.theatreoffice.daos.TheatreDAO;
import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Theatre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class TheatreController {
	@Autowired
	private TheatreDAO theatreDAO;
	@Autowired
	private ScheduleDAO scheduleDAO;

	@GetMapping("/theatres")
	public String theatresMain(Model model) {
		List<Theatre> theatres = theatreDAO.getAllTheatres();
		model.addAttribute("theatres", theatres);
		return "theatres-main";
	}

	@GetMapping("/theatres/add")
	public String theatresAdd(Model model) {
		return "theatre-add";
	}

	@PostMapping("/theatres/add")
	public String theatresAdd(@RequestParam String name, @RequestParam int seats_ground_floor,
								  @RequestParam int seats_balcony, @RequestParam int seats_mezzanine,
								  @RequestParam int building, @RequestParam String street,
								  @RequestParam String town, @RequestParam String country, Model model) {
		Theatre theatre = new Theatre(name, seats_ground_floor, seats_balcony, seats_mezzanine, building, street, town, country);
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

		model.addAttribute("theatre", theatre);
		model.addAttribute("schedule", schedule);
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
								@RequestParam String name, @RequestParam int seats_ground_floor,
								@RequestParam int seats_balcony, @RequestParam int seats_mezzanine,
								@RequestParam int building, @RequestParam String street,
								@RequestParam String town, @RequestParam String country, Model model) {
		Theatre theatre = theatreDAO.getTheatreById(id).orElseThrow();

		theatre.setBuilding(building);
		theatre.setStreet(street);
		theatre.setTown(town);
		theatre.setCountry(country);

		theatre.setName(name);
		theatre.setSeatsGroundFloor(seats_ground_floor);
		theatre.setSeatsBalcony(seats_balcony);
		theatre.setSeatsMezzanine(seats_mezzanine);
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
