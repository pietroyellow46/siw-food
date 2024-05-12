package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Chef;

public interface ChefRepository extends CrudRepository<Chef, Long> {
	
	public List<Chef> findByName(String name);
	public List<Chef> findBySurname(String surname);
	public List<Chef> findByNameOrSurname(String name, String surname);
}