package it.uniroma3.siw.controller;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import it.uniroma3.siw.validator.RecipeValidator;
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

	@Autowired
	private RecipeValidator recipeValidator;


	//pagina con lista ricette
	@GetMapping("/recipe")
	public String getRecipes(Model model) {		
		model.addAttribute("recipes", this.recipeService.findAll());
		return "recipes.html";
	}

	//pagina con dettagli di una ricetta
	@GetMapping("/recipe/{id}")
	public String getRecipe(@PathVariable("id") Long id, Model model) {
		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);
		model.addAttribute("proc",recipe.getSplittedProcedure());
		return "recipe.html";
	}

	//form per nuova ricetta
	@GetMapping("chef/formNewRecipe")
	public String formNewRecipe(Model model) {
		model.addAttribute("recipe", new Recipe());
		return "chef/formNewRecipe.html";
	}

	//raccoglie post per nuova ricetta
	@PostMapping("chef/recipe")
	public String newRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,BindingResult bindingResult, Model model,
			@RequestParam("primaryImage") MultipartFile mainMultipartFile, @RequestParam("extraImage") MultipartFile[] extraMultipartFiles) throws IOException {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if (!credentials.getRole().equals(Credentials.ADMIN_ROLE)) { 	//se non è admin setti chef della ricetta a chef loggato
			Chef chef = credentials.getUser();							//se chi inserisce la ricetta è admin allora la crea senza chef
			recipe.setChef(chef);
		}

		this.recipeValidator.validate(recipe, bindingResult);
		if (bindingResult.hasErrors()) {
			System.err.println(bindingResult.getAllErrors().toString());// sono emersi errori nel binding​
			return "chef/formNewRecipe.html";
		}
		else {

			this.recipeService.save(recipe);
			String uploadDir = "./images/recipe/"+recipe.getId();

			if (!mainMultipartFile.isEmpty()) {
				String mainImageName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
				String newMainFileName = recipe.getId()+"Main."+MvcConfig.getExtension(mainImageName);
				recipe.setMainImage(newMainFileName);
				MvcConfig.saveUploadFile(uploadDir, mainMultipartFile, newMainFileName);
			}

			if (!extraMultipartFiles[0].isEmpty()) {
				String extraImageName1 = StringUtils.cleanPath(extraMultipartFiles[0].getOriginalFilename());
				String newExtraImage1 = recipe.getId()+"Extra1."+MvcConfig.getExtension(extraImageName1);
				recipe.setExtraImage1(newExtraImage1);
				MvcConfig.saveUploadFile(uploadDir, extraMultipartFiles[0], newExtraImage1);
			}

			if (!extraMultipartFiles[1].isEmpty()) {
				String extraImageName2 = StringUtils.cleanPath(extraMultipartFiles[1].getOriginalFilename());
				String newExtraImage2 = recipe.getId()+"Extra2."+MvcConfig.getExtension(extraImageName2);
				recipe.setExtraImage2(newExtraImage2);
				MvcConfig.saveUploadFile(uploadDir, extraMultipartFiles[1], newExtraImage2);
			}

			if (!extraMultipartFiles[2].isEmpty()) {
				String extraImageName3 = StringUtils.cleanPath(extraMultipartFiles[2].getOriginalFilename());
				String newExtraImage3 = recipe.getId()+"Extra3."+MvcConfig.getExtension(extraImageName3);
				recipe.setExtraImage3(newExtraImage3);
				MvcConfig.saveUploadFile(uploadDir, extraMultipartFiles[2], newExtraImage3);
			}

			this.recipeService.save(recipe);
			model.addAttribute(recipe);
			System.out.println("\n\n" +recipe.getDescription()+ "\n\n");
			return "redirect:/recipe/"+recipe.getId();
		}
	}


	//pagina con form per cercare una ricetta
	@GetMapping("/searchRecipe")
	public String searchRecipe(Model model) {
		return "formSearchRecipe.html";
	}

	//raccoglie con stringa del nome ricetta da cercare
	@PostMapping("/searchRecipe")
	public String foundRecipe(Model model, @RequestParam String nome) {
		model.addAttribute("recipes", this.recipeService.findByName(nome));
		return "foundRecipe.html";
	}

	//path da poter chiamare nella barra del browser per cercare una ricetta
	@GetMapping("/searchRecipe/{name}")
	public String searchRecipe(@PathVariable("name") String name, Model model) {
		String realName = name.replace("_", " ");
		return foundRecipe(model, realName);
	}

	//pagina con tutte le ricette da poter modificare (solo le tue se non sei admin, tutte se sei admin)
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

	//pagina con dettagli di una ricetta da poter modificare
	@GetMapping("chef/formUpdateRecipe/{id}")
	public String formUpdateRecipe(@PathVariable("id") Long id, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();
		boolean isAdmin = credentials.getRole().equals(Credentials.ADMIN_ROLE);

		Recipe recipe = this.recipeService.findById(id);
		model.addAttribute("recipe", recipe);

		//se sei admin ok
		if(isAdmin) {
			return "admin/formUpdateRecipe.html";
		}
		else {
			boolean isMineRecipe = this.recipeService.isRecipeofChef(id, idChef);
			System.out.println(isMineRecipe);
			if (isMineRecipe) { //se la ricetta è tua ok
				return "chef/formUpdateRecipe.html";
			}
			else { //se non sei ne admin ne la ricetta è tua errore
				return "error.html";
			}
		}
	}

	//pagina per scegliere quale chef mettere per la ricetta con id nel path
	@GetMapping("admin/addChef/{id}")
	public String addChef(@PathVariable("id") Long id, Model model) {
		model.addAttribute("chef", this.chefService.findAllNotAdmin());
		model.addAttribute("idRecipe",id);
		return "admin/addChef.html";
	}

	//metodo che setta lo chef di una ricetta
	@GetMapping("admin/setChefToRecipe/{idChef}/{idRecipe}")
	public String setChefToRecipe(@PathVariable("idChef") Long idChef,@PathVariable("idRecipe") Long idRecipe) {
		Recipe recipe = recipeService.findById(idRecipe);
		recipe.setChef(this.chefService.findById(idChef));
		
		if (recipeService.existsByNomeAndChef(recipe.getNome(), recipe.getChef()))
			return "redirect:/admin/addChef/"+idRecipe+"?error=true";
		this.recipeService.save(recipe);
		return "redirect:/chef/formUpdateRecipe/"+idRecipe;
	}

	//pagina per aggiungere un ingrediente a una ricetta
	@GetMapping("chef/updateIngredient/{id}")
	public String updateIngredients(@PathVariable("id") Long id, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();

		boolean isMineRecipe = this.recipeService.isRecipeofChef(id, idChef);

		//se sei admin o la ricetta è tua ok sennò errore
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

	//form per mettere quantita e unita di misura dell'ingrediente da aggiungere alla ricetta
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

	//raccoglie la post per aggiungere un ingrediente alla ricetta
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

			//se la ricetta è tua o sei admin om sennò errore
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

	//rimuove un ingrediente da una ricetta
	@GetMapping("chef/removeIngredientFromRecipe/{usedIngredientId}")
	public String removeIngredientfromRecipe(@PathVariable("usedIngredientId") Long usedIngredientId, Model model) {
		UsedIngredient ui = this.usedIngredientService.findById(usedIngredientId);
		Long recipeId = ui.getRecipe().getId();
		this.usedIngredientService.delete(ui);
		return "redirect:/chef/updateIngredient/"+recipeId;
	}

	//restituisce pagina per modificare attributi di una ricetta
	@GetMapping("chef/updateRecipe/{recipeId}")
	public String updateRecipe(@PathVariable("recipeId") Long recipeId, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();

		boolean isMineRecipe = this.recipeService.isRecipeofChef(recipeId, idChef);

		//se la ricetta è tua o sei admin ok sennò errore
		if(isMineRecipe || credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			Recipe recipe = recipeService.findById(recipeId);
			model.addAttribute("recipe", recipe);
			model.addAttribute("recipeId", recipeId);
			return "chef/formChangeRecipe.html";
		}


		return "error.html";
	}

	//raccoglie la post per modificare la ricetta con i nuovi attributi
	@PostMapping("chef/recipe/{recipeId}")
	public String changeRecipe(@Valid @ModelAttribute("recipe") Recipe recipe,BindingResult bindingResult, Model model, @PathVariable("recipeId") Long recipeId,
			@RequestParam("primaryImage") MultipartFile mainMultipartFile, @RequestParam("extraImage") MultipartFile[] extraMultipartFiles) throws IOException{

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();

		boolean isMineRecipe = this.recipeService.isRecipeofChef(recipeId, idChef);

		if(isMineRecipe || credentials.getRole().equals(Credentials.ADMIN_ROLE)) {

			Recipe oldRecipe = recipeService.findById(recipeId);
			recipe.setId(recipeId);
			recipe.setChef(oldRecipe.getChef());
			recipe.setUsedIngredients(oldRecipe.getUsedIngredients());
			
			this.recipeValidator.validate(recipe, bindingResult);
			if (bindingResult.hasErrors()) {
				return "redirect:/chef/updateRecipe/"+recipeId+"?error=true";
			}else {

				String uploadDir = "./images/recipe/"+recipe.getId();

				if (!mainMultipartFile.isEmpty()) {
					String mainImageName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
					String newMainFileName = recipe.getId()+"Main."+MvcConfig.getExtension(mainImageName);
					recipe.setMainImage(newMainFileName);
					MvcConfig.saveUploadFile(uploadDir, mainMultipartFile, newMainFileName);
				} else
					recipe.setMainImage(oldRecipe.getMainImage());

				if (!extraMultipartFiles[0].isEmpty()) {
					String extraImageName1 = StringUtils.cleanPath(extraMultipartFiles[0].getOriginalFilename());
					String newExtraImage1 = recipe.getId()+"Extra1."+MvcConfig.getExtension(extraImageName1);
					recipe.setExtraImage1(newExtraImage1);
					MvcConfig.saveUploadFile(uploadDir, extraMultipartFiles[0], newExtraImage1);
				} else
					recipe.setExtraImage1(oldRecipe.getExtraImage1());

				if (!extraMultipartFiles[1].isEmpty()) {
					String extraImageName2 = StringUtils.cleanPath(extraMultipartFiles[1].getOriginalFilename());
					String newExtraImage2 = recipe.getId()+"Extra2."+MvcConfig.getExtension(extraImageName2);
					recipe.setExtraImage2(newExtraImage2);
					MvcConfig.saveUploadFile(uploadDir, extraMultipartFiles[1], newExtraImage2);
				} else
					recipe.setExtraImage2(oldRecipe.getExtraImage2());

				if (!extraMultipartFiles[2].isEmpty()) {
					String extraImageName3 = StringUtils.cleanPath(extraMultipartFiles[2].getOriginalFilename());
					String newExtraImage3 = recipe.getId()+"Extra3."+MvcConfig.getExtension(extraImageName3);
					recipe.setExtraImage3(newExtraImage3);
					MvcConfig.saveUploadFile(uploadDir, extraMultipartFiles[2], newExtraImage3);
				} else
					recipe.setExtraImage3(oldRecipe.getExtraImage3());
								
				this.recipeService.save(recipe);
				return "redirect:/chef/formUpdateRecipe/"+recipeId;
			}
		}
		else {
			return "error.html";
		}
	}

	//metodo che rimuove la ricetta con id del path
	@GetMapping("chef/removeRecipe/{recipeId}")
	public String removeRecipe(@PathVariable("recipeId") Long recipeId, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Long idChef = credentials.getUser().getId();

		boolean isMineRecipe = this.recipeService.isRecipeofChef(recipeId, idChef);

		//se la ricetta è tua o admin pk sennò errore
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