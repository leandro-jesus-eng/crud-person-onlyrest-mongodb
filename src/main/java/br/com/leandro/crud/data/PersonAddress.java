package br.com.leandro.crud.data;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode (callSuper = true)

@Document
public class PersonAddress extends AbstractEntity {
	
	public PersonAddress() {
		super();
	}
	
	public PersonAddress(String address, String city, String stateProvince, String postalCode, Country country) {
		super();
		this.address = address;
		this.city = city;
		this.stateProvince = stateProvince;
		this.postalCode = postalCode;
		this.country = country;
	}

	@Id
	private String id;
	
	@Field
	private String address;
	
	@Field
	private String city;
	
	@Field
	private String stateProvince;
	
	@Field
	private String postalCode;
	
	@DBRef (lazy = false) // @ManyToOne (fetch = FetchType.EAGER)
	private Country country;	
	
	// @ManyToOne (fetch = FetchType.EAGER)
	/*@ToString.Exclude
	@EqualsAndHashCode.Exclude
	@JsonBackReference
	private Person person;*/
}
