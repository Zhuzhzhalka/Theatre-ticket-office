package com.example.theatreoffice.controllers;

import com.example.theatreoffice.models.Location;
import com.example.theatreoffice.models.Schedule;
import com.example.theatreoffice.models.Theatre;
import com.example.theatreoffice.repos.LocationRepo;
import com.example.theatreoffice.repos.ScheduleRepo;
import com.example.theatreoffice.repos.TheatreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TheatreController {
	@Autowired
	private TheatreRepo theatreRepo;
	@Autowired
	private LocationRepo locationRepo;
	@Autowired
	private ScheduleRepo scheduleRepo;

	@GetMapping("/theatres")
	public String theatresMain(Model model) {
		Iterable<Theatre> theatres = theatreRepo.findAll();
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
		Location location = new Location(building, street, town, country);
		locationRepo.save(location);

		Theatre theatre = new Theatre(name, location, seats_ground_floor, seats_balcony, seats_mezzanine);
		theatreRepo.save(theatre);

		return "redirect:/theatres";
	}

	@GetMapping("/theatres/{id}")
	public String theatreDetails(@PathVariable(value = "id") long id, Model model) {
		if (!(theatreRepo.existsById(id))) {
			return "redirect:/theatres";
		}

		Optional<Theatre> theatre = theatreRepo.findById(id);
		ArrayList<Theatre> res = new ArrayList<>();
		theatre.ifPresent(res::add);

		List<Schedule> schedule = scheduleRepo.findByTheatre(res.get(0));

		model.addAttribute("theatre", res);
		model.addAttribute("schedule", schedule);
		return "theatre-details";
	}

	@GetMapping("/theatres/{id}/edit")
	public String theatreEdit(@PathVariable(value = "id") long id, Model model) {
		if (!(theatreRepo.existsById(id))) {
			return "redirect:/theatres";
		}

		Optional<Theatre> theatre = theatreRepo.findById(id);
		ArrayList<Theatre> res = new ArrayList<>();
		theatre.ifPresent(res::add);

		model.addAttribute("theatre", res);
		return "theatre-edit";
	}

	@PostMapping("/theatres/{id}/edit")
	public String theatreUpdate(@PathVariable(value = "id") long id,
								@RequestParam String name, @RequestParam int seats_ground_floor,
								@RequestParam int seats_balcony, @RequestParam int seats_mezzanine,
								@RequestParam int building, @RequestParam String street,
								@RequestParam String town, @RequestParam String country, Model model) {
		Theatre theatre = theatreRepo.findById(id).orElseThrow();

		Location location = theatre.getLocation();
		location.setBuilding(building);
		location.setStreet(street);
		location.setTown(town);
		location.setCountry(country);
		locationRepo.save(location);

		theatre.setName(name);
		theatre.setLocation(location);
		theatre.setSeatsGroundFloor(seats_ground_floor);
		theatre.setSeatsBalcony(seats_balcony);
		theatre.setSeatsMezzanine(seats_mezzanine);
		theatreRepo.save(theatre);

		return "redirect:/theatres/{id}";
	}

	@PostMapping("/theatres/{id}/remove")
	public String theatreRemove(@PathVariable(value = "id") long id, Model model) {
		Theatre theatre = theatreRepo.findById(id).orElseThrow();
		theatreRepo.delete(theatre);

		return "redirect:/theatres";
	}
}
