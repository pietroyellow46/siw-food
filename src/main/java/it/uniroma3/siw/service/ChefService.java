package it.uniroma3.siw.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.repository.ChefRepository;


@Service
public class ChefService {

	@Autowired
	private ChefRepository chefRepository;

	public Chef findById(Long id) {
		return chefRepository.findById(id).get();
	}

	public List<Chef> findAll() {
		return (List<Chef>) chefRepository.findAll();
	}

	public void save(Chef chef) {
		chefRepository.save(chef);
	}
	
	public List<Chef> findByName(String name){
		return chefRepository.findByName(name);
	}
	
	public List<Chef> findBySurname(String surname){
		return chefRepository.findBySurname(surname);
	}
	
	public List<Chef> findByNameOrSurname(String name, String surname){
		return chefRepository.findByNameOrSurname(name,surname);
	}

}