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

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;
import jakarta.validation.Valid;

@Controller
public class IngredientController {
	
	@Autowired
	private IngredientService ingredientService;
	@Autowired
	private RecipeService recipeService;

	@GetMapping("/ingredient")
	public String getIngredients(Model model) {		
		model.addAttribute("ingredients", this.ingredientService.findAll());
		return "ingredients.html";
	}
	
	@GetMapping("/ingredient/{id}")
	public String getIngredient(@PathVariable("id") Long id, Model model) {
		model.addAttribute("ingredient", this.ingredientService.findById(id));
		return "ingredient.html";
	}
	
	@GetMapping("admin/formNewIngredient")
	public String formNewIngredient(Model model) {
		model.addAttribute("ingredient", new Ingredient());
		return "admin/formNewIngredient.html";
	}

	@PostMapping("admin/ingredient")
	public String newIngredient(@Valid @ModelAttribute("ingredient") Ingredient ingredient,BindingResult bindingResult, Model model){
		// this.movieValidator.validate(movie, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "admin/formNewIngredient.html";
		} else {
			this.ingredientService.save(ingredient);
			model.addAttribute(ingredient);
			return "redirect:/ingredient/"+ingredient.getId();
		}
	}
	
	@GetMapping("/searchIngredient")
	public String searchChef(Model model) {
		return "formSearchIngredient.html";
	}
	
	@PostMapping("/searchIngredient")
	public String foundChef(Model model, @RequestParam String name) {
		model.addAttribute("ingredients", this.ingredientService.findByName(name));
		return "foundIngredient.html";
	}
	
	@GetMapping("/searchIngredient/{name}")
	public String searchChef(@PathVariable("name") String name, Model model) {
		return foundChef(model, name);
	}
	
	@GetMapping("admin/removeIngredient/{ingredientId}")
	public String removeRecipe(@PathVariable("ingredientId") Long ingredientId, Model model) {
		Ingredient i = ingredientService.findById(ingredientId);
		this.ingredientService.deleteIngredient(i);
		return "redirect:/admin/manageIngredient";
	}
	
	@GetMapping("admin/manageIngredient")
	public String manageIngredient(Model model) {
		model.addAttribute("ingredients", this.ingredientService.findAll());
		return "admin/manageIngredient.html";
	}
	
	@GetMapping("admin/formUpdateIngredient/{id}")
	public String formUpdateIngredient(@PathVariable("id") Long id, Model model) {
		Ingredient i = this.ingredientService.findById(id);
		model.addAttribute("ingredient", i);
		model.addAttribute("recipes", this.recipeService.findRecipeWithIngredient(id));
		return "admin/formUpdateIngredient.html";
	}
	
	@GetMapping("admin/changeIngredient/{ingredientId}")
	public String updateRecipe(@PathVariable("ingredientId") Long ingredientId, Model model) {
		Ingredient ingredient = ingredientService.findById(ingredientId);
		model.addAttribute("ingredient", ingredient);
		model.addAttribute("ingredientId", ingredientId);
		return "admin/formChangeIngredient.html";
	}
	
	@PostMapping("admin/ingredient/{ingredientId}")
	public String changeRecipe(@Valid @ModelAttribute("ingredient") Ingredient ingredient,BindingResult bindingResult, Model model, @PathVariable("ingredientId") Long ingredientId){
		Ingredient oldIngredient = ingredientService.findById(ingredientId);
		ingredient.setId(ingredientId);
		ingredient.setUsedIngredients(oldIngredient.getUsedIngredients());
		ingredientService.save(ingredient);
		return "redirect:/admin/formUpdateIngredient/"+ingredientId;
	}


}