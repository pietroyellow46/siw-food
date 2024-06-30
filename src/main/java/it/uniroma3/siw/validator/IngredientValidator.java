package it.uniroma3.siw.validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Ingredient;
import it.uniroma3.siw.service.IngredientService;


@Component
public class IngredientValidator implements Validator {
	@Autowired
	private IngredientService ingredientService;

	@Override //verifica ingredienti omonimi
	public void validate(Object o, Errors errors) {
		Ingredient ingredient = (Ingredient)o;
		if (ingredient.getName()!=null ) {
			if (ingredient.getId()==null && ingredientService.existsByName(ingredient.getName())) //verifica esistenza di altro ingrediente con stesso nome
				errors.reject("ingredient.duplicate");
			if (ingredient.getId()!=null && ingredientService.verificaEsistenzaIngredienteConNome(ingredient)) //verifica esistenza di altro ingrediente con stesso nome ma id diverso
				errors.reject("ingredient.duplicate");

		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return Ingredient.class.equals(aClass);
	}
}
