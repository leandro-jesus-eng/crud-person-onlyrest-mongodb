package br.com.leandro.crud;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.leandro.crud.controller.ResourceNotFoundException;
import br.com.leandro.crud.data.Country;
import br.com.leandro.crud.data.Gender;
import br.com.leandro.crud.data.Person;
import br.com.leandro.crud.data.PersonAddress;
import br.com.leandro.crud.service.PersonService;

@SpringBootTest
@AutoConfigureMockMvc
class CrudPersonControllerTests implements CommandLineRunner {

	@Autowired
	MongoTemplate mongoTemplate;	 
		
	@Autowired
	PersonService personService;
	
	@Autowired
	MockMvc mockMvc;
	
	@Test
	void whenFindAll() throws Exception {
		createData();
		
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person")
			      .accept(MediaType.APPLICATION_JSON))
			      .andDo(MockMvcResultHandlers.print())
			      .andExpect(MockMvcResultMatchers.status().isOk())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").exists())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("Leandro"))
			      .andExpect(MockMvcResultMatchers.jsonPath("$.[*].id").isNotEmpty())
			      .andExpect(MockMvcResultMatchers.jsonPath("$.[*].personAddresses[*]").isNotEmpty());
		
		destroyData();
	}
	
	@Test
	void whenSave() throws Exception {
				
		Person person;
		Calendar c = Calendar.getInstance();		
		Country country = new Country(null, "Brasil");
		mongoTemplate.save(country);
		
		person = new Person();
		person.setFirstName("Maximo");
		person.setLastName("Cristaldo");
		c.set(1915, Calendar.OCTOBER, 28);
		person.setBirthday(c.getTime());
		person.setGender(Gender.MALE);
		Set<PersonAddress> pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("Morada do Sossego", "Campo Grande", "MS", "79200-001", country) );
		person.setPersonAddresses( pa );
				
		//System.out.println(asJsonString( person ));
		
		mockMvc.perform( MockMvcRequestBuilders
			      .post("/REST/person")
			      .content(asJsonString( person ))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isCreated())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Maximo"))
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.personAddresses[*]").isNotEmpty());
		
		destroyData();
		
	}
	
	@Test
	void whenFindById() throws Exception {
		createData();
		
		Person person;
		Calendar c = Calendar.getInstance();		
		Country country = new Country(null, "Brasil");
		mongoTemplate.save(country);
		
		person = new Person();
		person.setFirstName("Maximo");
		person.setLastName("Cristaldo");
		c.set(1915, Calendar.OCTOBER, 28);
		person.setBirthday(c.getTime());
		person.setGender(Gender.MALE);
		Set<PersonAddress> pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("Morada do Sossego", "Campo Grande", "MS", "79200-001", country) );
		person.setPersonAddresses( pa );
		
		// Salvo um pessoa para depois pesquisar via API
		person = personService.save(person);
		
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person/{id}", person.getId())			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isOk())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Maximo"))
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.personAddresses[*]").isNotEmpty());
		
		// procura por um id q não existe
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person/{id}", "id_q_nao_existe")			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isNoContent());		
		
		destroyData();
	}
	
	@Test
	void whenReplace() throws Exception {
		createData();
		
		Person person;
		Calendar c = Calendar.getInstance();		
		Country country = new Country(null, "Brasil");
		mongoTemplate.save(country);
		
		person = new Person();
		person.setFirstName("Maximo");
		person.setLastName("Cristaldo");
		c.set(1915, Calendar.OCTOBER, 28);
		person.setBirthday(c.getTime());
		person.setGender(Gender.MALE);
		Set<PersonAddress> pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("Morada do Sossego", "Campo Grande", "MS", "79200-001", country) );
		person.setPersonAddresses( pa );
		
		// Salvo uma pessoa
		person = personService.save(person);
		
		
		// vou fazer uma alteração no nome da pessoa 
		person.setFirstName("Max");
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/REST/person/{id}", person.getId() )
			      .content(asJsonString( person ))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isOk());
		// verifica se alterou mesmo
		person = personService.findById(person.getId());
		assertEquals("Max", person.getFirstName());
		
		// se id null MethodNotAllowed
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/REST/person/{id}","")
			      .content(asJsonString( person ))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
		mockMvc.perform( MockMvcRequestBuilders
			      .put("/REST/person")
			      .content(asJsonString( person ))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isMethodNotAllowed());
		
		destroyData();
	}
	
	
	@Test
	void whenDelete() throws Exception {
		createData();
		
		Person person;
		Calendar c = Calendar.getInstance();		
		Country country = new Country(null, "Brasil");
		mongoTemplate.save(country);
		
		person = new Person();
		person.setFirstName("Maximo");
		person.setLastName("Cristaldo");
		c.set(1915, Calendar.OCTOBER, 28);
		person.setBirthday(c.getTime());
		person.setGender(Gender.MALE);
		Set<PersonAddress> pa = new HashSet<PersonAddress>();
		pa.add( new PersonAddress("Morada do Sossego", "Campo Grande", "MS", "79200-001", country) );
		person.setPersonAddresses( pa );
		
		// Salvo uma pessoa
		person = personService.save(person);
		
		// deleta a pessoa salva usando API
		mockMvc.perform( MockMvcRequestBuilders
			      .delete("/REST/person/{id}", person.getId() )
			      .content(asJsonString( person ))
			      .contentType(MediaType.APPLICATION_JSON)
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isOk());		
		// verifica se removeu
		try {
			person = personService.findById(person.getId());
			fail();
		} catch (ResourceNotFoundException e) {
			assertEquals(PersonService.COULD_NOT_FIND+person.getId(), e.getMessage());
		}
		
		// tenta remover uma pessoa q não existe
		mockMvc.perform( MockMvcRequestBuilders
			      .delete("/REST/person/{id}", "id_q_nao_existe")			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isNoContent());		
		
		destroyData();
	}
	
	@Test
	void whenFindByFirstName() throws Exception {
		createData();
		
		// procura todos q tenham a letra i no nome
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person/firstname/{firstName}", "i")			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isOk())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").exists())
			      	.andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("Felipe"));
			      	
		
		// procura por um nome q não existe
		mockMvc.perform( MockMvcRequestBuilders
			      .get("/REST/person/firstname/{firstName}", "nome_q_nao_existe")			      
			      .accept(MediaType.APPLICATION_JSON))		
			      	.andDo(MockMvcResultHandlers.print())
			      	.andExpect(MockMvcResultMatchers.status().isNoContent());		
		
		
		destroyData();
	}

	@Override
	public void run(String... args) throws Exception {
		destroyData();
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
		
		personService.findAll().forEach( e -> personService.delete(e) );
		personService.findAllCountries().forEach( ct -> mongoTemplate.remove(ct) );
	}
	
	public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}
}
