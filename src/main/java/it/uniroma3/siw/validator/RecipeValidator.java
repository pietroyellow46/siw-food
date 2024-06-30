package it.uniroma3.siw.validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Recipe;
import it.uniroma3.siw.service.RecipeService;


@Component
public class RecipeValidator implements Validator {
	
  @Autowired
  private RecipeService recipeService;

  @Override //verifica ricette omonime
  public void validate(Object o, Errors errors) {
    Recipe recipe = (Recipe)o;
    if (recipe.getNome()!=null && recipe.getChef()!=null) {
    	if (recipe.getId()==null && recipeService.existsByNomeAndChef(recipe.getNome(), recipe.getChef())) //verifica esistenza ricetta di chef e nome
    		errors.reject("recipe.duplicate");
    	if(recipe.getId()!=null && recipeService.verificaEsistenzaRicettaConNomeEChef(recipe)) //verifica esistenza di ricetta con id diverso ma stesso nome e chef
    		errors.reject("recipe.duplicate");
    }
  }
  
  @Override
  public boolean supports(Class<?> aClass) {
      return Recipe.class.equals(aClass);
    }
}
