package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.model.UsedIngredient;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.service.UsedIngredientService;
import jakarta.validation.Valid;

@Controller
public class RecipeController {

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private CredentialsService credentialsService;

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

	@GetMapping("chef/formNewRecipe")
	public String formNewRecipe(Model model) {
		model.addAttribute("recipe", new Recipe());
		return "chef/formNewRecipe.html";
	}

	@PostMapping("chef/recipe")
	public String newRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,BindingResult bindingResult, Model model){
		// this.movieValidator.validate(movie, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "chef/formNewRecipe.html";
		} else {
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (!credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				Chef chef = credentials.getUser();
				recipe.setChef(chef);
			}
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
		String realName = name.replace("_", " ");
		return foundRecipe(model, realName);
	}

	@GetMapping("chef/manageRecipe")
	public String manageRecipe(Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());		
		boolean isAdmin = credentials.getRole().equals(Credentials.ADMIN_ROLE);

		if (isAdmin) {
			model.addAttribute("recipes", this.recipeService.findAll());
		}
		else {
			Long idChef = credentials.getUser().getId();
			model.addAttribute("recipes", this.recipeService.findByIdChef(idChef));
		}		
		return "chef/manageRecipe.html";
	}

	@GetMapping("chef/formUpdateRecipe/{id}")
	public String formUpdateMovies(@PathVariable("id") Long id, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();
		boolean isAdmin = credentials.getRole().equals(Credentials.ADMIN_ROLE);

		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);

		if(isAdmin) {
			return "admin/formUpdateRecipe.html";
		}
		else {
			boolean isMineRecipe = this.recipeService.isRecipeofChef(id, idChef);
			System.out.println(isMineRecipe);
			if (isMineRecipe) {
				return "chef/formUpdateRecipe.html";
			}
			else {
				return "error.html";
			}
		}
	}

	@GetMapping("admin/addChef/{id}")
	public String addChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findAllNotAdmin());
		model.addAttribute("idRecipe",id);
		return "admin/addChef.html";
	}

	@GetMapping("admin/setChefToRecipe/{idChef}/{idRecipe}")
	public String setChefToRecipe(@PathVariable("idChef") Long idChef,@PathVariable("idRecipe") Long idRecipe) {
		Recipe recipe = recipeService.findById(idRecipe);
		recipe.setChef(this.chefService.findById(idChef));
		this.recipeService.save(recipe);
		return "redirect:/chef/formUpdateRecipe/"+idRecipe;
	}

	@GetMapping("chef/updateIngredient/{id}")
	public String updateIngredients(@PathVariable("id") Long id, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();

		boolean isMineRecipe = this.recipeService.isRecipeofChef(id, idChef);

		if (isMineRecipe || credentials.getRole().equals(Credentials.ADMIN_ROLE) ) {
			List<Ingredient> ingredientsToAdd = this.ingredientsService.findIngredientNotInRecipe(id);
			model.addAttribute("ingredientsToAdd", ingredientsToAdd);
			model.addAttribute("recipe", this.recipeService.findById(id));

			return "chef/updateIngredient.html";
		}
		else {
			return "error.html";
		}

	}

	@GetMapping("chef/formNewIngredient/{ingredientId}/{recipeId}")
	public String addIngredientToRecipe(@PathVariable("ingredientId") Long ingredientId, @PathVariable("recipeId") Long recipeId, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();

		boolean isMineRecipe = this.recipeService.isRecipeofChef(recipeId, idChef);

		if(isMineRecipe || credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			UsedIngredient newUsedIngredient = new UsedIngredient();
			newUsedIngredient.setIngredient(this.ingredientsService.findById(ingredientId));
			newUsedIngredient.setRecipe(this.recipeService.findById(recipeId));
			model.addAttribute("newUsedIngredient",newUsedIngredient);
			model.addAttribute("ingredientId",ingredientId);
			model.addAttribute("recipeId",recipeId);
			return "chef/formNewUsedIngredient.html";
		}
		else {
			return "error.html";
		}
	}

	@PostMapping("chef/usedIngredient/{ingredientId}/{recipeId}")
	public String newUsedIngredient(@Valid @ModelAttribute("newUsedIngredient") UsedIngredient usedIngredient,BindingResult bindingResult, Model model, @PathVariable("ingredientId") Long ingredientId, @PathVariable("recipeId") Long recipeId){
		// this.movieValidator.validate(movie, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "chef/formNewUsedIngredient.html";
		} else {

			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			Long idChef = credentials.getUser().getId();

			boolean isMineRecipe = this.recipeService.isRecipeofChef(recipeId, idChef);

			if(isMineRecipe || credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				usedIngredient.setIngredient(this.ingredientsService.findById(ingredientId));
				usedIngredient.setRecipe(this.recipeService.findById(recipeId));
				this.usedIngredientService.save(usedIngredient);
				return "redirect:/chef/updateIngredient/"+recipeId;
			}
			else {
				return "error.html";
			}
		}
	}

	@GetMapping("chef/removeIngredientFromRecipe/{usedIngredientId}")
	public String removeIngredientfromRecipe(@PathVariable("usedIngredientId") Long usedIngredientId, Model model) {
		UsedIngredient ui = this.usedIngredientService.findById(usedIngredientId);
		Long recipeId = ui.getRecipe().getId();
		this.usedIngredientService.delete(ui);
		System.out.println("\n\n\n\n"+this.recipeService.findById(recipeId).getUsedIngredients().size()+"\n\n\n\n");
		return "redirect:/chef/updateIngredient/"+recipeId;
	}

	@GetMapping("chef/updateRecipe/{recipeId}")
	public String updateRecipe(@PathVariable("recipeId") Long recipeId, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();

		boolean isMineRecipe = this.recipeService.isRecipeofChef(recipeId, idChef);

		if(isMineRecipe || credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			Recipe recipe = recipeService.findById(recipeId);
			model.addAttribute("recipe", recipe);
			model.addAttribute("recipeId", recipeId);
			return "chef/formChangeRecipe.html";
		}

		return "error.html";
	}

	@PostMapping("chef/recipe/{recipeId}")
	public String changeRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,BindingResult bindingResult, Model model, @PathVariable("recipeId") Long recipeId){

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();

		boolean isMineRecipe = this.recipeService.isRecipeofChef(recipeId, idChef);

		if(isMineRecipe || credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			Recipe oldRecipe = recipeService.findById(recipeId);
			System.out.println(recipeId);
			recipe.setId(recipeId);
			recipe.setChef(oldRecipe.getChef());
			recipe.setUsedIngredients(oldRecipe.getUsedIngredients());
			recipeService.save(recipe);
			return "redirect:/chef/formUpdateRecipe/"+recipeId;
		}
		else {
			return "error.html";
		}
	}

	@GetMapping("chef/removeRecipe/{recipeId}")
	public String removeRecipe(@PathVariable("recipeId") Long recipeId, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();

		boolean isMineRecipe = this.recipeService.isRecipeofChef(recipeId, idChef);

		if(isMineRecipe || credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			Recipe recipe = recipeService.findById(recipeId);
			this.recipeService.deleteRecipe(recipe);
			return "redirect:/chef/manageRecipe";
		}
		else {
			return "error.html";
		}


	}
}