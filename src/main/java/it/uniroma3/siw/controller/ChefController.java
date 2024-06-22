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

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.ChefService;
import it.uniroma3.siw.service.RecipeService;
import jakarta.validation.Valid;

@Controller
public class ChefController {

	@Autowired
	private ChefService chefService;

	@Autowired
	private RecipeService recipeService;
	
	@GetMapping("/homepage")
	public String homepage(Model model) {	
		Recipe recipe = this.recipeService.findById(Integer.toUnsignedLong(151));
		
		model.addAttribute("proc",recipe.getSplittedProcedure());

	
		model.addAttribute("recipe",recipe);
		return "homepage.html";
	}

	//ritorna lista tutti chef non admin
	@GetMapping("/allChef")
	public String getAllChef(Model model) {		
		model.addAttribute("chef", this.chefService.findAllNotAdmin());
		return "allChef.html";
	}

	//ritorna dettagli su uno chef
	@GetMapping("/allChef/{id}")
	public String getChef(@PathVariable("id") Long id, Model model) {
		Chef chef = this.chefService.findById(id);
		model.addAttribute("chef", chef);
		model.addAttribute("credentials", chef.getCredential());
		model.addAttribute("recipes", this.recipeService.findByIdChef(id));		
		return "chef.html";
	}

	//ritorna form per cercare uno chef
	@GetMapping("/searchChef")
	public String searchChef(Model model) {
		return "formSearchChef.html";
	}

	//cerca gli chef che hanno nome o cognome contenente la stringa inviata
	@PostMapping("/searchChef")
	public String foundChef(Model model, @RequestParam String nameSurname) {
		model.addAttribute("chef", this.chefService.findByNameOrSurname(nameSurname, nameSurname));
		return "foundChef.html";
	}

	//path a cui accedere senza passare dal browser per cercare gli chef
	@GetMapping("/searchChef/{nameSurname}")
	public String searchChef(@PathVariable("nameSurname") String nameSurname, Model model) {
		String realNameSurname = nameSurname.replace("_", " ");
		List<Chef> chefView = this.chefService.findByNameOrSurname(realNameSurname, realNameSurname);
		chefView.addAll(this.chefService.findNomeCognome(realNameSurname));
		model.addAttribute("chef", chefView);
		return "foundChef.html";
	}

	//pagina con lista chef che pupi modificare, non gli admin
	@GetMapping("admin/manageChef")
	public String manageChef(Model model) {
		model.addAttribute("chef", this.chefService.findAllNotAdmin());
		return "admin/manageChef.html";
	}

	//pagina per modificare uno chef
	@GetMapping("admin/formUpdateChef/{id}")
	public String formUpdateIngredient(@PathVariable("id") Long id, Model model) {
		Chef c = this.chefService.findById(id);
		model.addAttribute("chef", c);
		model.addAttribute("recipes", this.recipeService.findByIdChef(id));
		return "admin/formUpdateChef.html";
	}

	//rimuove lo chef
	@GetMapping("admin/removeChef/{chefId}")
	public String removeChef(@PathVariable("chefId") Long chefId, Model model) {
		this.chefService.deleteById(chefId);
		return "redirect:/admin/manageChef";
	}

	//pagina per modificare i dettagli di uno chef
	@GetMapping("admin/changeChef/{chefId}")
	public String updateRecipe(@PathVariable("chefId") Long chefId, Model model) {
		Chef chef = chefService.findById(chefId);
		model.addAttribute("chef", chef);
		model.addAttribute("chefId", chefId);
		return "admin/formChangeChef.html";
	}

	//dettagli dello chef aggiornato con foto
	@PostMapping("admin/chef/{chefId}")
	public String changeChef(@Valid @ModelAttribute("chef") Chef chef,BindingResult bindingResult, Model model,
			@PathVariable("chefId") Long chefId, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException{

		if (!bindingResult.hasErrors()) {

			Chef oldChef = chefService.findById(chefId);
			chef.setId(chefId);
			chef.setRecipes(oldChef.getRecipes());
			chef.setPathImage(oldChef.getPathImage());

			//se non ho passato nulla lascio la vecchia foto
			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

				String newFileName = "chef"+chef.getId()+"."+MvcConfig.getExtension(fileName);
				chef.setPathImage(newFileName);

				String uploadDir="./images/chef";

				MvcConfig.saveUploadFile(uploadDir, multipartFile, newFileName);
			}

			this.chefService.save(chef);
			return "redirect:/admin/formUpdateChef/"+chefId;
		}
		else {
			return "redirect:/admin/changeChef/"+chefId+"?error=true";
		}
	}

	@GetMapping("admin/formNewChef")
	public String formNewChef(Model model) {
		model.addAttribute("chef", new Chef());
		return "admin/formNewChef.html";
	}


	@PostMapping("admin/chef")
	public String newChef(@Valid @ModelAttribute("chef") Chef chef,BindingResult bindingResult,
			Model model, @RequestParam("fileImage") MultipartFile multipartFile) throws IOException{
		// this.movieValidator.validate(movie, bindingResult);
		if (bindingResult.hasErrors()) { // sono emersi errori nel binding​
			return "admin/formNewChef.html";
		} else {

			if (!multipartFile.isEmpty()) {
				String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
				this.chefService.save(chef);

				String newFileName = "chef"+chef.getId()+"."+MvcConfig.getExtension(fileName);
				chef.setPathImage(newFileName);
				this.chefService.save(chef);

				String uploadDir="./images/chef";

				MvcConfig.saveUploadFile(uploadDir, multipartFile, newFileName);
			}
			else {
				this.chefService.save(chef);
			}
			model.addAttribute("chef", chef);
			return "redirect:/allChef/"+chef.getId();
		}
	}
}