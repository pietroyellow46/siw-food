package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.UsedIngredient;

public interface UsedIngredientRepository extends CrudRepository<UsedIngredient, Long> {
}