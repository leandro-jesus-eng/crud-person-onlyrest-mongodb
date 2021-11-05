package br.com.leandro.crud.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.leandro.crud.controller.ResourceNotFoundException;
import br.com.leandro.crud.data.Country;
import br.com.leandro.crud.data.Person;
import br.com.leandro.crud.repository.PersonRepository;

@Service
@Transactional
public class PersonService {
	
	public static String COULD_NOT_FIND = "Could not find person = "; 
	
	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	public List<Person> findAll() {
		return personRepository.findAll();		
	}
	
	public Person save(Person person) {		
		return personRepository.save(person);
	}
	
	public void delete(Person person) {
		personRepository.delete(person);
	}
	
	public void deleteById(String id) {
		Person p = findById(id);
		//p.setId(id);
		personRepository.delete(p);
	}

	public Person findById(String id) {		
		return personRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException(COULD_NOT_FIND + id) );		
	}
	
	public Person replace (Person newPerson, String id) {
		if(id == null) new br.com.leandro.crud.controller.IllegalArgumentException("ID parameter cannot be null");
		
		return personRepository.findById(id).map(person -> {			
			person.setFirstName(newPerson.getFirstName());
			person.setLastName(newPerson.getLastName());
			person.setBirthday(newPerson.getBirthday());
			person.setGender(newPerson.getGender());
			person.setPersonAddresses(newPerson.getPersonAddresses());
			person.setPersonImages(newPerson.getPersonImages());			
			return personRepository.save(person);
		}).orElseGet(() -> {
			new ResourceNotFoundException("Could not find person id = "+id);			
			return null;
		});
	}
	
	public List<Person> findByFirstName(String firstName) {		
		List<Person> lp = personRepository.findByFirstName(firstName).orElseThrow( () -> new ResourceNotFoundException(COULD_NOT_FIND + firstName) );
		if(lp.isEmpty())
			throw new ResourceNotFoundException(COULD_NOT_FIND + firstName);			
		return lp; 				
	}
	
	public List<Country> findAllCountries() {
		return mongoTemplate.findAll(Country.class);
	}
	
}
