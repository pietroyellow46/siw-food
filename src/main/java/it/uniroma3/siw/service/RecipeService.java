package it.uniroma3.siw.service;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.repository.RecipeRepository;

@Service
public class RecipeService {

	@Autowired
	private RecipeRepository recipeRepository;

	//cerca per id
	public Recipe findById(Long id) {
		return recipeRepository.findById(id).get();
	}

	//ritorna tutte
	public List<Recipe> findAll() {
		return (List<Recipe>) recipeRepository.findAll();
	}

	//salva
	public void save(Recipe recipe) {
		recipeRepository.save(recipe);
	}
	
	//ricette dello chef di id passato
	public List<Recipe> findByIdChef(Long idChef){
		return this.recipeRepository.findByIdChef(idChef);
	}
	
	//ricette di chef passato
	public List<Recipe> findByChef(Chef Chef){
		return this.recipeRepository.findByChef(Chef);
	}

	//cerca per nome
	public List<Recipe> findByName(String name) {
		return this.recipeRepository.findByNomeContaining(name);
	}

	//elimina ricetta
	public void deleteRecipe(Recipe recipe) {
		this.recipeRepository.delete(recipe);
	}

	//ricette con ingrediente di id passato
	public List<Recipe> findRecipeWithIngredient(Long ingredientId){
		return this.recipeRepository.findRecipeWithIngredient(ingredientId);
	}
	
	//dice se la ricetta è dello chef passato
	public boolean isRecipeofChef(Long idRecipe, Long idChef) {
		return this.recipeRepository.isRecipeofChef(idRecipe, idChef);
	}
}