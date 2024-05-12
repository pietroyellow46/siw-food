package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.service.ChefService;
import jakarta.validation.Valid;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;

	@GetMapping("/chef")
	public String getAllChef(Model model) {		
		model.addAttribute("chef", this.chefService.findAll());
		return "allChef.html";
	}

	@GetMapping("/chef/{id}")
	public String getChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findById(id));
		return "chef.html";
	}

	@GetMapping("admin/formNewChef")
	public String formNewChef(Model model) {
		model.addAttribute("chef", new Chef());
		return "admin/formNewChef.html";
	}
	
	@GetMapping("/searchChef")
	public String searchChef(Model model) {
		return "formSearchChef.html";
	}
	
	@PostMapping("/searchChef")
	public String foundChef(Model model, @RequestParam String nameSurname) {
		model.addAttribute("chef", this.chefService.findByNameOrSurname(nameSurname, nameSurname));
		return "foundChef.html";
	}
	
	@GetMapping("/searchChef/{nameSurname}")
	public String searchChef(@PathVariable("nameSurname") String nameSurname, Model model) {
		return foundChef(model, nameSurname);
	}

	@PostMapping("admin/chef")
	public String newChef(@Valid @ModelAttribute("chef") Chef chef,BindingResult bindingResult, Model model){
		// this.movieValidator.validate(movie, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "admin/formNewChef.html";
		} else {
			this.chefService.save(chef);
			model.addAttribute("chef", chef);
			return "redirect:/chef/"+chef.getId();
		}
	}



}