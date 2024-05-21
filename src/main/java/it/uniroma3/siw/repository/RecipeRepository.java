package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.model.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
	
	public List<Recipe> findByNome(String name);
		
	public List<Recipe> findByNomeContaining(String nome);
	
	public List<Recipe> findByChef(Chef chef);
	
	@Query(value = "SELECT * FROM RECIPE WHERE chef_id = :chefId", nativeQuery = true)
	public List<Recipe> findByIdChef(@Param("chefId") Long id);	

	@Query(value = "SELECT recipe.* FROM recipe JOIN used_ingredient ON recipe.id = used_ingredient.recipe_id JOIN ingredient ON used_ingredient.ingredient_id = ingredient.id WHERE ingredient.id = :ingredientId", nativeQuery = true)
	public List<Recipe> findRecipeWithIngredient(@Param("ingredientId") Long id);
	
	@Query(value = "SELECT EXISTS (SELECT 1 FROM recipe WHERE chef_id = :idChef AND id = :idRecipe) AS ricetta_presente", nativeQuery = true)
	public boolean isRecipeofChef(@Param("idRecipe") Long idRecipe,@Param("idChef") Long idChef);
}