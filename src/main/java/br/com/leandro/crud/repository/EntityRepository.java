package br.com.leandro.crud.repository;

public interface EntityRepository<T> {
	
	public long generateSequence(String seqName);
	
	<S extends T> S save(S entity);	
}
