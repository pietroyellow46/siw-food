package it.uniroma3.siw.controller;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;
import it.uniroma3.siw.validator.IngredientValidator;
import jakarta.validation.Valid;

@Controller
public class IngredientController {

	@Autowired
	private IngredientService ingredientService;

	@Autowired
	private RecipeService recipeService;

	@Autowired
	private IngredientValidator ingredientValidator;
	


	//lista ingredienti
	@GetMapping("/ingredient")
	public String getIngredients(Model model) {		
		model.addAttribute("ingredients", this.ingredientService.findAll());
		return "ingredients.html";
	}

	//ritorna dettagli di un ingrediente
	@GetMapping("/ingredient/{id}")
	public String getIngredient(@PathVariable("id") Long id, Model model) {
		Ingredient ingredient = this.ingredientService.findById(id);
		model.addAttribute("ingredient", ingredient);
		model.addAttribute("recipes", this.recipeService.findRecipeWithIngredient(id));
		return "ingredient.html";
	}

	//form per nuovo ingrediente
	@GetMapping("admin/formNewIngredient")
	public String formNewIngredient(Model model) {
		model.addAttribute("ingredient", new Ingredient());
		return "admin/formNewIngredient.html";
	}

	//post per nuovo ingrediente
	@PostMapping("admin/ingredient")
	public String newIngredient(@Valid @ModelAttribute("ingredient") Ingredient ingredient,BindingResult bindingResult,
			Model model, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException{
		this.ingredientValidator.validate(ingredient, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "admin/formNewIngredient.html";
		} else {
			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				this.ingredientService.save(ingredient);

				String newFileName = "ingredient"+ingredient.getId()+"."+MvcConfig.getExtension(fileName);
				ingredient.setPathImage(newFileName);
				this.ingredientService.save(ingredient);

				String uploadDir="./images/ingredient";

				MvcConfig.saveUploadFile(uploadDir, multipartFile, newFileName);
			}
			else {
				this.ingredientService.save(ingredient);
			}
			model.addAttribute(ingredient);
			return "redirect:/ingredient/"+ingredient.getId();
		}
	}

	//form per ricerca ingrediente
	@GetMapping("/searchIngredient")
	public String searchIngredient(Model model) {
		return "formSearchIngredient.html";
	}

	//raccoglie la post con il nome da cercare
	@PostMapping("/searchIngredient")
	public String foundIngredient(Model model, @RequestParam String name) {
		model.addAttribute("ingredients", this.ingredientService.findByName(name));
		return "foundIngredient.html";
	}

	//path da poter chiamare direttamente dalla barra del browser che cerca l'ingrediente (con underscore a posto degli spazi)
	@GetMapping("/searchIngredient/{name}")
	public String searchIngredient(@PathVariable("name") String name, Model model) {
		String newName = name.replace("_", " ");
		return foundIngredient(model, newName);
	}

	//rimuove l'ingrediente con l'id del path
	@GetMapping("admin/removeIngredient/{ingredientId}")
	public String removeIngredient(@PathVariable("ingredientId") Long ingredientId, Model model) {
		Ingredient i = ingredientService.findById(ingredientId);
		MvcConfig.deleteFile("./images/ingredient/"+i.getPathImage());
		this.ingredientService.deleteIngredient(i);
		return "redirect:/admin/manageIngredient";
	}

	//ritorna alla pagina per la gestione degli ingredienti
	@GetMapping("admin/manageIngredient")
	public String manageIngredient(Model model) {
		model.addAttribute("ingredients", this.ingredientService.findAll());
		return "admin/manageIngredient.html";
	}

	//ritorna la pagina per la modifica di un ingrediente dell'id del path
	@GetMapping("admin/formUpdateIngredient/{id}")
	public String formUpdateIngredient(@PathVariable("id") Long id, Model model) {
		Ingredient i = this.ingredientService.findById(id);
		model.addAttribute("ingredient", i);
		model.addAttribute("recipes", this.recipeService.findRecipeWithIngredient(id));
		return "admin/formUpdateIngredient.html";
	}

	//form per modificare i dettagli dell'ingrediente con id del path
	@GetMapping("admin/changeIngredient/{ingredientId}")
	public String updateIngredient(@PathVariable("ingredientId") Long ingredientId, Model model) {
		Ingredient ingredient = ingredientService.findById(ingredientId);
		model.addAttribute("ingredient", ingredient);
		model.addAttribute("ingredientId", ingredientId);
		return "admin/formChangeIngredient.html";
	}

	//raccoglie i nuovi attributi per la modifica dell ingreidente
	@PostMapping("admin/ingredient/{ingredientId}")
	public String changeIngredient(@Valid @ModelAttribute("ingredient") Ingredient ingredient,BindingResult bindingResult, Model model,
			@PathVariable("ingredientId") Long ingredientId,@RequestParam("fileImage") MultipartFile multipartFile) throws IOException{
		Ingredient oldIngredient = ingredientService.findById(ingredientId);
		ingredient.setId(ingredientId);
		ingredient.setUsedIngredients(oldIngredient.getUsedIngredients());
		ingredient.setPathImage(oldIngredient.getPathImage());

		this.ingredientValidator.validate(ingredient, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "redirect:/admin/changeIngredient/"+ingredientId+"?error=true";
		}
		else {
			if (!multipartFile.isEmpty()) { //se vuoto lasci la vecchia foto
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

				String newFileName = "ingredient"+ingredient.getId()+"."+MvcConfig.getExtension(fileName);
				ingredient.setPathImage(newFileName);

				String uploadDir="./images/ingredient";

				MvcConfig.saveUploadFile(uploadDir, multipartFile, newFileName);
			}
			this.ingredientService.save(ingredient);
			return "redirect:/admin/formUpdateIngredient/"+ingredientId;
		}
	}
}