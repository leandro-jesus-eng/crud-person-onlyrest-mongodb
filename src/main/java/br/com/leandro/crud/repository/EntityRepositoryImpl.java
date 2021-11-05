package br.com.leandro.crud.repository;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import br.com.leandro.crud.data.DatabaseSequence;

public class EntityRepositoryImpl<T> implements EntityRepository<T> {

	//@Autowired
	//private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Override
	public long generateSequence(String seqName) {		
		DatabaseSequence counter = mongoOperations.findAndModify( 
				new Query(Criteria.where("_id").is(seqName)),				 
				new Update().inc("seq",1),
				new FindAndModifyOptions().returnNew(true).upsert(true),
			    DatabaseSequence.class);
		return !Objects.isNull(counter) ? counter.getSeq().longValue() : 1;
	}

	@Override
	public <S extends T> S save(S entity) {				
		//AbstractEntity ae = (AbstractEntity) entity;		
		/* ativar para eu controlar a geração de ID, ou ele vai gerar sozinho im sequencia de carcateres */
		//Long id = generateSequence(entity.getClass().getSimpleName());		
		//ae.setId(id.toString());		
		
		mongoOperations.save(entity);
		
		return (S) entity;
	}

}
