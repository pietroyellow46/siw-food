package it.uniroma3.siw.controller;

import java.util.List;

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
import it.uniroma3.siw.service.RecipeService;
import jakarta.validation.Valid;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;

	@Autowired
	private RecipeService recipeService;
	
	@GetMapping("/allChef")
	public String getAllChef(Model model) {		
		model.addAttribute("chef", this.chefService.findAllNotAdmin());
		return "allChef.html";
	}

	@GetMapping("/allChef/{id}")
	public String getChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findById(id));
		model.addAttribute("recipes", this.recipeService.findByIdChef(id));
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
		String realNameSurname = nameSurname.replace("_", " ");
		List<Chef> chefView = this.chefService.findByNameOrSurname(realNameSurname, realNameSurname);
		chefView.addAll(this.chefService.findNomeCognome(realNameSurname));
		model.addAttribute("chef", chefView);
		return "foundChef.html";
	}

	@PostMapping("admin/chef")
	public String newChef(@Valid @ModelAttribute("chef") Chef chef,BindingResult bindingResult, Model model){
		// this.movieValidator.validate(movie, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "admin/formNewChef.html";
		} else {
			this.chefService.save(chef);
			model.addAttribute("chef", chef);
			return "redirect:/allChef/"+chef.getId();
		}
	}
	
	@GetMapping("admin/manageChef")
	public String manageChef(Model model) {
		model.addAttribute("chef", this.chefService.findAllNotAdmin());
		return "admin/manageChef.html";
	}
	
	@GetMapping("admin/formUpdateChef/{id}")
	public String formUpdateIngredient(@PathVariable("id") Long id, Model model) {
		Chef c = this.chefService.findById(id);
		model.addAttribute("chef", c);
		model.addAttribute("recipes", this.recipeService.findByIdChef(id));
		return "admin/formUpdateChef.html";
	}
	
	@GetMapping("admin/removeChef/{chefId}")
	public String removeChef(@PathVariable("chefId") Long chefId, Model model) {
		this.chefService.deleteById(chefId);
		return "redirect:/admin/manageChef";
	}
	
	@GetMapping("admin/changeChef/{chefId}")
	public String updateRecipe(@PathVariable("chefId") Long chefId, Model model) {
		Chef chef = chefService.findById(chefId);
		model.addAttribute("chef", chef);
		model.addAttribute("chefId", chefId);
		return "admin/formChangeChef.html";
	}
	
	@PostMapping("admin/chef/{chefId}")
	public String changeRecipe(@Valid @ModelAttribute("chef") Chef chef,BindingResult bindingResult, Model model, @PathVariable("chefId") Long chefId){
		Chef oldChef = chefService.findById(chefId);
		chef.setId(chefId);
		chef.setRecipes(oldChef.getRecipes());
		chefService.save(chef);
		return "redirect:/admin/formUpdateChef/"+chefId;
	}



}