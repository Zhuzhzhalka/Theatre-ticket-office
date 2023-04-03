package com.example.theatreoffice.controllers;

import com.example.theatreoffice.daos.PerformanceDAO;
import com.example.theatreoffice.models.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
	@Autowired
	private PerformanceDAO performanceDAO;

	@GetMapping("/")
	public String home(Model model) {
		List<Performance> performances = performanceDAO.getPerformancesOrderedByRating(true).subList(0, 9);
		model.addAttribute("title", "Main page");
		model.addAttribute("performances", performances);
		return "home";
	}
	
	@GetMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About us");
		return "about";
	}

}
