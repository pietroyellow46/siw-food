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

	public Recipe findById(Long id) {
		return recipeRepository.findById(id).get();
	}

	public List<Recipe> findAll() {
		return (List<Recipe>) recipeRepository.findAll();
	}

	public void save(Recipe recipe) {
		recipeRepository.save(recipe);
	}
	
	public List<Recipe> findByIdChef(Long idChef){
		return this.recipeRepository.findByIdChef(idChef);
	}
	
	public List<Recipe> findByChef(Chef Chef){
		return this.recipeRepository.findByChef(Chef);
	}

	public List<Recipe> findByName(String name) {
		return this.recipeRepository.findByNomeContaining(name);
	}

	public void deleteRecipe(Recipe recipe) {
		this.recipeRepository.delete(recipe);
	}

	public List<Recipe> findRecipeWithIngredient(Long ingredientId){
		return this.recipeRepository.findRecipeWithIngredient(ingredientId);
	}
	
	public boolean isRecipeofChef(Long idRecipe, Long idChef) {
		return this.recipeRepository.isRecipeofChef(idRecipe, idChef);
	}
}