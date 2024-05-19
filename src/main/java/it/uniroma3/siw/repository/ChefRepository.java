package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Chef;

public interface ChefRepository extends CrudRepository<Chef, Long> {
	
	public List<Chef> findByName(String name);
	public List<Chef> findBySurname(String surname);
	public List<Chef> findByNameOrSurname(String name, String surname);
	
	@Query(value = "SELECT * FROM chef WHERE name || ' ' || surname = :nomeCognome OR surname || ' ' || name = :nomeCognome", nativeQuery = true)
	public List<Chef> findNomeCognome(@Param("nomeCognome") String nomeCognome);
	
	@Query(value = "SELECT chef.* FROM chef WHERE chef.id NOT IN (SELECT credentials.user_id FROM credentials WHERE credentials.role = 'ADMIN')", nativeQuery = true)
	public List<Chef> findAllNotAdmin();
	
}