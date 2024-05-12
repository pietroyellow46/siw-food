package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
	
	public List<Ingredient> findByName(String name);
	
	@Query(value="select * from ingredient where id not in (select ingredient_id from used_ingredient where recipe_id = (select id from recipe where id = :recipeId))", nativeQuery=true)
	public List<Ingredient> findIngredientNotInRecipe(@Param("recipeId") Long id);


}