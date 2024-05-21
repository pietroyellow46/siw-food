package it.uniroma3.siw.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
import it.uniroma3.siw.service.IngredientService;
import it.uniroma3.siw.service.RecipeService;
import jakarta.validation.Valid;

/*object-fit*/
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

		Ingredient ingredient = this.ingredientService.findById(id);
		//ingredient.retrivePathImage();
		model.addAttribute("ingredient", ingredient);
		model.addAttribute("recipes", this.recipeService.findRecipeWithIngredient(id));
		return "ingredient.html";
	}

	@GetMapping("chef/formNewIngredient")
	public String formNewIngredient(Model model) {
		model.addAttribute("ingredient", new Ingredient());
		return "chef/formNewIngredient.html";
	}

	@PostMapping("chef/ingredient")
	public String newIngredient(@Valid @ModelAttribute("ingredient") Ingredient ingredient,BindingResult bindingResult,
			Model model, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException{
		// this.movieValidator.validate(movie, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "chef/formNewIngredient.html";
		} else {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			this.ingredientService.save(ingredient);
			System.out.println(multipartFile.isEmpty());
			int lastDotIndex = fileName.lastIndexOf('.');
			String estensione = fileName.substring(lastDotIndex + 1);

			String newFileName = "ingredient"+ingredient.getId()+"."+estensione;
			ingredient.setPathImage(newFileName);
			this.ingredientService.save(ingredient);

			String uploadDir="./images/ingredient";

			Path uploadPath = Paths.get(uploadDir);

			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			try(InputStream inputStream = multipartFile.getInputStream()){
				Path filePath = uploadPath.resolve(newFileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				throw new IOException("Could not save upload file: " + fileName);
			}

			model.addAttribute(ingredient);
			return "redirect:/ingredient/"+ingredient.getId();
		}
	}

	@GetMapping("/searchIngredient")
	public String searchIngredient(Model model) {
		return "formSearchIngredient.html";
	}

	@PostMapping("/searchIngredient")
	public String foundIngredient(Model model, @RequestParam String name) {
		model.addAttribute("ingredients", this.ingredientService.findByName(name));
		return "foundIngredient.html";
	}

	@GetMapping("/searchIngredient/{name}")
	public String searchIngredient(@PathVariable("name") String name, Model model) {
		String newName = name.replace("_", " ");
		return foundIngredient(model, newName);
	}

	@GetMapping("admin/removeIngredient/{ingredientId}")
	public String removeIngredient(@PathVariable("ingredientId") Long ingredientId, Model model) {
		Ingredient i = ingredientService.findById(ingredientId);
		MvcConfig.deleteFile("./images/ingredient/"+i.getPathImage());
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
	public String changeRecipe(@Valid @ModelAttribute("ingredient") Ingredient ingredient,BindingResult bindingResult, Model model,
			@PathVariable("ingredientId") Long ingredientId,@RequestParam("fileImage") MultipartFile multipartFile) throws IOException{
		Ingredient oldIngredient = ingredientService.findById(ingredientId);
		ingredient.setId(ingredientId);
		ingredient.setUsedIngredients(oldIngredient.getUsedIngredients());		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		this.ingredientService.save(ingredient);

		int lastDotIndex = fileName.lastIndexOf('.');
		String estensione = fileName.substring(lastDotIndex + 1);

		String newFileName = "ingredient"+ingredient.getId()+"."+estensione;
		ingredient.setPathImage(newFileName);
		this.ingredientService.save(ingredient);

		String uploadDir="./images/ingredient";

		Path uploadPath = Paths.get(uploadDir);

		if (!Files.exists(uploadPath)) {
			Files.createDirectories(uploadPath);
		}

		try(InputStream inputStream = multipartFile.getInputStream()){
			Path filePath = uploadPath.resolve(newFileName);
			Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			throw new IOException("Could not save upload file: " + fileName);
		}
		
		
		return "redirect:/admin/formUpdateIngredient/"+ingredientId;
	}


}