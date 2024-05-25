package it.uniroma3.siw.controller;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;


@RestController
public class ApiController {

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private ChefService chefService;

	@Autowired
	private IngredientService ingredientService;


	@GetMapping("/rest/recipe/{recipeId}")
	public Recipe getRecipe(@PathVariable("recipeId") Long recipeId) {
		return this.recipeService.findById(recipeId);
	}

	@GetMapping("/rest/recipe")
	public List<Recipe> getRecipes() {
		return this.recipeService.findAll();
	}

	@GetMapping("/rest/chef/{chefId}")
	public Chef getChef(@PathVariable("chefId") Long chefId) {
		return this.chefService.findById(chefId);
	}

	@GetMapping("/rest/chef")
	public List<Chef> getAllChef() {
		return this.chefService.findAll();
	}

	@GetMapping("/rest/ingredient/{ingredientId}")
	public Ingredient getIngredient(@PathVariable("ingredientId") Long ingredientId) {
		return this.ingredientService.findById(ingredientId);
	}

	@GetMapping("/rest/ingredient")
	public List<Ingredient> getAllIngredient() {
		return this.ingredientService.findAll();
	}
}
