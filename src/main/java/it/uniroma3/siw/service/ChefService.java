package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Chef;
import it.uniroma3.siw.repository.ChefRepository;


@Service
public class ChefService {

	@Autowired
	private ChefRepository chefRepository;

	//cerca per id
	public Chef findById(Long id) {
		return chefRepository.findById(id).get();
	}

	//cerca tutti
	public List<Chef> findAll() {
		return (List<Chef>) chefRepository.findAll();
	}
	
	//cerca tutti non admin
	public List<Chef> findAllNotAdmin(){
		return this.chefRepository.findAllNotAdmin();
	}
	
	//cerca per nome o cognome
	public List<Chef> findNomeCognome(String nomeCognome){
		return this.chefRepository.findNomeCognome(nomeCognome);
	}

	//salva
	public void save(Chef chef) {
		chefRepository.save(chef);
	}
	
	//cerca per nome
	public List<Chef> findByName(String name){
		return chefRepository.findByName(name);
	}
	
	//cerca per cognome
	public List<Chef> findBySurname(String surname){
		return chefRepository.findBySurname(surname);
	}
	
	//cerca per nome o cognome
	public List<Chef> findByNameOrSurname(String name, String surname){
		return chefRepository.findByNameSurnameInsensitive(name);
	}
	
	//elimina chef di id passato
	public void deleteById(Long id) {
		this.chefRepository.deleteById(id);
	}
	
    @Transactional
    public Chef getUser(Long id) {
        Optional<Chef> result = this.chefRepository.findById(id);
        return result.orElse(null);
    }


    @Transactional
    public Chef saveUser(Chef user) {
        return this.chefRepository.save(user);
    }

    @Transactional
    public List<Chef> getAllUsers() {
        List<Chef> result = new ArrayList<>();
        Iterable<Chef> iterable = this.chefRepository.findAll();
        for(Chef user : iterable)
            result.add(user);
        return result;
    }

}