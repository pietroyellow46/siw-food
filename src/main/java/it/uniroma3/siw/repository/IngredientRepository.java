package it.uniroma3.siw.repository;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

	public List<Ingredient> findByName(String name); //cerca ingredienti per nome

	//ritorna ingredienti che non sono nella ricetta con id passato
	@Query(value="select * from ingredient where id not in (select ingredient_id from used_ingredient where recipe_id = (select id from recipe where id = :recipeId))", nativeQuery=true)
	public List<Ingredient> findIngredientNotInRecipe(@Param("recipeId") Long id);
	
	@Query(value = "SELECT * FROM ingredient WHERE name ILIKE '%' || :nome || '%'", nativeQuery = true)
	public List<Ingredient> findByNameInsensitive(@Param("nome") String nome); //ricerca per nome o cognome

	//ritorna ingredienti con nome che contiene stringa passata
	public List<Ingredient> findByNameContaining(String name);	
	
	//verifica esistenza ingrediente di nome passato
	public boolean existsByName(String name);
	
	//verifica esistenza ingrediente di nome passato e id diverso
    boolean existsByNameAndIdNot(String name, Long id);
}