package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Chef;

public interface ChefRepository extends CrudRepository<Chef, Long> {
	
	public List<Chef> findByName(String name); //ricerca per nome
	public List<Chef> findBySurname(String surname); //ricerca per cognome
	public List<Chef> findByNameContainingOrSurnameContaining(String name, String surname); //ricerca per nome o cognome
	
	@Query(value = "SELECT * FROM chef WHERE name ILIKE '%' || :nomeCognome || '%' OR surname ILIKE '%' || :nomeCognome || '%' OR (name || ' ' || surname) ILIKE '%' || :nomeCognome || '%' OR (surname || ' ' || name) ILIKE '%' || :nomeCognome || '%'", nativeQuery = true)
	public List<Chef> findByNameSurnameInsensitive(@Param("nomeCognome") String nomeCognome); //ricerca per nome o cognome

	//query che restuisce chef che matchano se passi 'Nome Cognome'
	@Query(value = "SELECT * FROM chef WHERE name || ' ' || surname = :nomeCognome OR surname || ' ' || name = :nomeCognome", nativeQuery = true)
	public List<Chef> findNomeCognome(@Param("nomeCognome") String nomeCognome);
	
	//ritorna tutti gli chef non admin
	@Query(value = "SELECT chef.* FROM chef WHERE chef.id NOT IN (SELECT credentials.user_id FROM credentials WHERE credentials.role = 'ADMIN')", nativeQuery = true)
	public List<Chef> findAllNotAdmin();
	
}