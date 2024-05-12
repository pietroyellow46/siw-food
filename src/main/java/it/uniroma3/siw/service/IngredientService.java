package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.repository.IngredientRepository;

@Service
public class IngredientService {

	@Autowired
	private IngredientRepository ingredientRepository;

	public Ingredient findById(Long id) {
		return ingredientRepository.findById(id).get();
	}

	public List<Ingredient> findAll() {
		return (List<Ingredient>) ingredientRepository.findAll();
	}

	public void save(Ingredient ingredient) {
		ingredientRepository.save(ingredient);
	}
	
	public List<Ingredient> findByName(String name) {
		return this.ingredientRepository.findByName(name);
	}
	
	public List<Ingredient> findIngredientNotInRecipe(Long idRecipe){
		return this.ingredientRepository.findIngredientNotInRecipe(idRecipe);
	}
	
	public void deleteIngredient(Ingredient ingredient) {
		this.ingredientRepository.delete(ingredient);
	}

}