package br.com.leandro.crud.data;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode (callSuper = true)
@Document(collection = "database_sequences")
public class DatabaseSequence extends AbstractEntity {
	@Id
	private String id;
	
	@Column
    private BigInteger seq;
}
