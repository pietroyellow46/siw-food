package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.model.UsedIngredient;
import it.uniroma3.siw.repository.UsedIngredientRepository;


@Service
public class UsedIngredientService {

	@Autowired
	private UsedIngredientRepository usedIngredientRepository;

	public UsedIngredient findById(Long id) {
		return usedIngredientRepository.findById(id).get();
	}

	public Iterable<UsedIngredient> findAll() {
		return usedIngredientRepository.findAll();
	}

	public void save(UsedIngredient usedIngredient) {
		usedIngredientRepository.save(usedIngredient);
	}
	public void delete(UsedIngredient usedIngredient) {
		this.usedIngredientRepository.delete(usedIngredient);
	}

}