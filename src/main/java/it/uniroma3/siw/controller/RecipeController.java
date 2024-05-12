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

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.UsedIngredient;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.service.UsedIngredientService;
import jakarta.validation.Valid;

@Controller
public class RecipeController {
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private ChefService chefService;
	
	@Autowired
	private IngredientService ingredientsService;
	
	@Autowired
	private UsedIngredientService usedIngredientService;

	@GetMapping("/recipe")
	public String getRecipes(Model model) {		
		model.addAttribute("recipes", this.recipeService.findAll());
		return "recipes.html";
	}
	
	@GetMapping("/recipe/{id}")
	public String getRecipe(@PathVariable("id") Long id, Model model) {
		model.addAttribute("recipe", this.recipeService.findById(id));
		return "recipe.html";
	}
	
	@GetMapping("admin/formNewRecipe")
	public String formNewRecipe(Model model) {
		model.addAttribute("recipe", new Recipe());
		return "admin/formNewRecipe.html";
	}

	@PostMapping("admin/recipe")
	public String newRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,BindingResult bindingResult, Model model){
		// this.movieValidator.validate(movie, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "admin/formNewRecipe.html";
		} else {
			this.recipeService.save(recipe);
			model.addAttribute(recipe);
			return "redirect:/recipe/"+recipe.getId();
		}
	}

	@GetMapping("/searchRecipe")
	public String searchRecipe(Model model) {
		return "formSearchRecipe.html";
	}
	
	@PostMapping("/searchRecipe")
	public String foundRecipe(Model model, @RequestParam String nome) {
		model.addAttribute("recipes", this.recipeService.findByName(nome));
		return "foundRecipe.html";
	}
	
	@GetMapping("/searchRecipe/{name}")
	public String searchRecipe(@PathVariable("name") String name, Model model) {
		return foundRecipe(model, name);
	}
	
	@GetMapping("admin/manageRecipe")
	public String manageRecipe(Model model) {
		model.addAttribute("recipes", this.recipeService.findAll());
		return "admin/manageRecipe.html";
	}
	
	@GetMapping("admin/formUpdateRecipe/{id}")
	public String formUpdateMovies(@PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		return "admin/formUpdateRecipe.html";
	}
	
	@GetMapping("admin/addChef/{id}")
	public String addChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findAll());
		model.addAttribute("idRecipe",id);
		return "admin/addChef.html";
	}
	
	@GetMapping("admin/setChefToRecipe/{idChef}/{idRecipe}")
	public String setChefToRecipe(@PathVariable("idChef") Long idChef,@PathVariable("idRecipe") Long idRecipe) {
		Recipe recipe = recipeService.findById(idRecipe);
		recipe.setChef(this.chefService.findById(idChef));
		this.recipeService.save(recipe);
		return "redirect:/admin/formUpdateRecipe/"+idRecipe;
	}
	
	@GetMapping("admin/updateIngredient/{id}")
	public String updateActors(@PathVariable("id") Long id, Model model) {

		List<Ingredient> ingredientsToAdd = this.ingredientsService.findIngredientNotInRecipe(id);
		model.addAttribute("ingredientsToAdd", ingredientsToAdd);
		model.addAttribute("recipe", this.recipeService.findById(id));

		return "admin/updateIngredient.html";
	}
	
	@GetMapping("admin/formNewIngredient/{ingredientId}/{recipeId}")
	public String addIngredientToRecipe(@PathVariable("ingredientId") Long ingredientId, @PathVariable("recipeId") Long recipeId, Model model) {
		UsedIngredient newUsedIngredient = new UsedIngredient();
		newUsedIngredient.setIngredient(this.ingredientsService.findById(ingredientId));
		newUsedIngredient.setRecipe(this.recipeService.findById(recipeId));
		model.addAttribute("newUsedIngredient",newUsedIngredient);
		model.addAttribute("ingredientId",ingredientId);
		model.addAttribute("recipeId",recipeId);
		return "admin/formNewUsedIngredient.html";
	}
	
	@PostMapping("admin/usedIngredient/{ingredientId}/{recipeId}")
	public String newUsedIngredient(@Valid @ModelAttribute("newUsedIngredient") UsedIngredient usedIngredient,BindingResult bindingResult, Model model, @PathVariable("ingredientId") Long ingredientId, @PathVariable("recipeId") Long recipeId){
		// this.movieValidator.validate(movie, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "admin/formNewUsedIngredient.html";
		} else {
			usedIngredient.setIngredient(this.ingredientsService.findById(ingredientId));
			usedIngredient.setRecipe(this.recipeService.findById(recipeId));
			this.usedIngredientService.save(usedIngredient);
			return "redirect:/admin/updateIngredient/"+recipeId;
		}
	}
	
	@GetMapping("admin/removeIngredientFromRecipe/{usedIngredientId}")
	public String removeIngredientfromRecipe(@PathVariable("usedIngredientId") Long usedIngredientId, Model model) {
		UsedIngredient ui = this.usedIngredientService.findById(usedIngredientId);
		Long recipeId = ui.getRecipe().getId();
		this.usedIngredientService.delete(ui);
		System.out.println("\n\n\n\n"+this.recipeService.findById(recipeId).getUsedIngredients().size()+"\n\n\n\n");
		return "redirect:/admin/updateIngredient/"+recipeId;
	}
	
	@GetMapping("admin/updateRecipe/{recipeId}")
	public String updateRecipe(@PathVariable("recipeId") Long recipeId, Model model) {
		Recipe recipe = recipeService.findById(recipeId);
		model.addAttribute("recipe", recipe);
		model.addAttribute("recipeId", recipeId);
		return "admin/formChangeRecipe.html";
	}
	
	@PostMapping("admin/recipe/{recipeId}")
	public String changeRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,BindingResult bindingResult, Model model, @PathVariable("recipeId") Long recipeId){
		Recipe oldRecipe = recipeService.findById(recipeId);
		System.out.println(recipeId);
		recipe.setId(recipeId);
		recipe.setChef(oldRecipe.getChef());
		recipe.setUsedIngredients(oldRecipe.getUsedIngredients());
		recipeService.save(recipe);
		return "redirect:/admin/formUpdateRecipe/"+recipeId;
	}
	
	@GetMapping("admin/removeRecipe/{recipeId}")
	public String removeRecipe(@PathVariable("recipeId") Long recipeId, Model model) {
		Recipe recipe = recipeService.findById(recipeId);
		this.recipeService.deleteRecipe(recipe);
		return "redirect:/admin/manageRecipe";
	}
}