package br.com.leandro.crud.data;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode (callSuper = true)

@Document (collection = "country")
public class Country extends AbstractEntity {
	
	public Country() {
		super();
	}
	
	public Country(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	@Id
    private String id;

    @Field
    private String name;
}
