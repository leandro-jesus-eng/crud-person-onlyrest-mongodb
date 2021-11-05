package br.com.leandro.crud.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
// @Getter @Setter // gera somente getters e setters
@EqualsAndHashCode (callSuper = true)

@Document (collection = "person")     //@Entity
public class Person extends AbstractEntity {
	
	
	public Person() {
		super();
	}
	
	public Person(String id) {
		super();
		this.id = id;
	}

	@Id
	private String id;
	
	@Field // @Column
	private String firstName;
	
	@Field // @Column  (columnDefinition = "VARCHAR(512)")
	private String lastName;
	
	@Enumerated(EnumType.STRING)
	// @Transient  -- desativa o atributo
	private Gender gender;
	
	@Field // @Column
	private Date birthday;
	
	//@OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	//@JoinColumn (name = "person_id")
	@ToString.Exclude
	//@JsonManagedReference
	private List<PersonImage> personImages = new ArrayList<PersonImage>();
	
	//@OneToMany (cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	//@JoinColumn (name = "person_id")
	@ToString.Exclude // Não gera toString() para entrar em loop com a outra clasee q também tem referência para cá.
	//@JsonManagedReference // para evitar loop no momento q vai serializar o objeto
	private Set<PersonAddress> personAddresses = new HashSet<PersonAddress>();

}
