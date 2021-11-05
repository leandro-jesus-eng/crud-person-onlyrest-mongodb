package br.com.leandro.crud;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.web.servlet.MockMvc;

import br.com.leandro.crud.data.Country;
import br.com.leandro.crud.data.Gender;
import br.com.leandro.crud.data.Person;
import br.com.leandro.crud.data.PersonAddress;
import br.com.leandro.crud.service.PersonService;

@SpringBootTest
@AutoConfigureMockMvc
class CrudPersonServiceTests implements CommandLineRunner {

	@Autowired
	MongoTemplate mongoTemplate;	 
		
	@Autowired
	PersonService personService;
	
	@Autowired
	MockMvc mockMvc;
	
	/*@Test
	void contextLoads() {		
	}*/
	
	@Test
	void testPersonServices() {	
		
		createData();		
		
		assertNotNull(personService);
		
		System.out.println("-------------------------");
		System.out.println("findAllCountries");
		System.out.println("-------------------------");
		personService.findAllCountries().forEach(System.out::println);
				
		findAll();
		
		List<Person> personList = personService.findAll();
		int index = Math.abs( (int)(Math.random()*1000) ) % personList.size();
		String id = personList.get( index ).getId();
		Person person = personService.findById(id);
		System.out.println("-------------------------");
		System.out.println("findById ="+id);
		System.out.println("-------------------------");
		System.out.println(person);

		// update o mesmo objeto						
		System.out.println("-------------------------");
		System.out.println("replace ="+id+"   First Name to João");
		System.out.println("-------------------------");
		person.setFirstName("João");
		personService.replace(person, person.getId());
		
		findAll();
		
		//replace 	
		System.out.println("-------------------------");
		System.out.println("replace ="+id+"   new id argument");
		System.out.println("-------------------------");
		person.setFirstName("João sem ID");
		id = "newid";
		personService.replace(person, id );
				
		findAll();
		
		//replace						
		System.out.println("-------------------------");
		System.out.println("replace ="+id+"   new id on object");
		System.out.println("-------------------------");
		person.setFirstName("João sem ID");
		person.setId("newid2");
		personService.replace(person, "newid");
						
		findAll();
		
		System.out.println("-------------------------");
		System.out.println("delete ="+id);
		System.out.println("-------------------------");		
		Person p = new Person(id);
		personService.delete( p );
		
		findAll();
		
		personList = personService.findAll();
		index = Math.abs( (int)(Math.random()*1000) ) % personList.size();
		id = personList.get( index ).getId();
		System.out.println("-------------------------");
		System.out.println("deleteById  ="+id);
		System.out.println("-------------------------");
		personService.deleteById(id);
		
		findAll();
		
		destroyData();
	}

	@Override
	public void run(String... args) throws Exception {
	}
	
	private void findAll() {
		System.out.println("-------------------------");
		System.out.println("findAll");
		System.out.println("-------------------------");
		// findAll
		List<Person> personList = personService.findAll();
		personList.forEach(System.out::println);		
	}
	
	private void createData() {
		
		Person person;
		Calendar c = Calendar.getInstance();
		
		Country countryUSA;
		Country countryBR;
		
		countryUSA = new Country(null, "USA");
		mongoTemplate.save(countryUSA);
		
		countryBR = new Country(null, "Brasil");
		mongoTemplate.save(countryBR);
		
		person = new Person();
		person.setFirstName("Leandro");
		person.setLastName("Jesus");
		c.set(1982, Calendar.JULY, 4);
		person.setBirthday(c.getTime());
		person.setGender(Gender.MALE);
		Set<PersonAddress> pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("707 Rosevelt ST", "Miami", "FLS", "30301", countryUSA) );
		person.setPersonAddresses( pa );				
		
		personService.save(person);
		
		person = new Person();
		person.setFirstName("Felipe");
		person.setLastName("Jesus");
		c.set(2014, Calendar.SEPTEMBER, 8);
		person.setBirthday(c.getTime());
		person.setGender(Gender.MALE);
		pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("Francisco Dias Feitosa 905", "Aquidauana", "MS", "79200-000", countryBR) );
		person.setPersonAddresses( pa );				
		
		personService.save(person);
		
		
		person = new Person();
		person.setFirstName("Marcia");
		person.setLastName("Cristaldo");
		c.set(1983, Calendar.JANUARY, 11);
		person.setBirthday(c.getTime());
		person.setGender(Gender.FEMALE);
		pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("Francisco Dias Feitosa 905", "Aquidauana", "MS", "79200-000", countryBR) );
		person.setPersonAddresses( pa );				
		
		personService.save(person);
	}
	
	private void destroyData() {
		
		System.out.println("-------------------------");
		System.out.println("removeAll");
		System.out.println("-------------------------");
		personService.findAll().forEach( e -> personService.delete(e) );		
		
		personService.findAllCountries().forEach( ct -> mongoTemplate.remove(ct) );
	}
}
