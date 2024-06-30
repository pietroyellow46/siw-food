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

	//cerca per id
	public Ingredient findById(Long id) {
		return ingredientRepository.findById(id).get();
	}

	//cerca tutti
	public List<Ingredient> findAll() {
		return (List<Ingredient>) ingredientRepository.findAll();
	}

	//salva
	public void save(Ingredient ingredient) {
		ingredientRepository.save(ingredient);
	}

	//cerca per nome
	public List<Ingredient> findByName(String name) {
		return this.ingredientRepository.findByNameInsensitive(name);
	}

	//cerca ingredienti non nella ricetta passata
	public List<Ingredient> findIngredientNotInRecipe(Long idRecipe){
		return this.ingredientRepository.findIngredientNotInRecipe(idRecipe);
	}

	//elimina per id
	public void deleteIngredient(Ingredient ingredient) {
		this.ingredientRepository.delete(ingredient);
	}

	//verifica esistenza di ingrediente con dato nome
	public boolean existsByName(String name) {
		return this.ingredientRepository.existsByName(name);
	}

	//verifica esistenza di ingrediente con id diverso ma nome uguale do quello passato
	public boolean verificaEsistenzaIngredienteConNome(Ingredient ingredient) {
		return ingredientRepository.existsByNameAndIdNot(ingredient.getName(), ingredient.getId());
	}
}