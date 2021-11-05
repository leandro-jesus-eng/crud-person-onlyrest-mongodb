package br.com.leandro.crud.data;

import io.swagger.v3.oas.annotations.Hidden;

public abstract class AbstractEntity {
	
	public abstract String getId();
	public abstract void setId(String id);
	
	@Override
	@Hidden
	public int hashCode() {
		if (getId() != null) return getId().hashCode();		
		return super.hashCode();
	}
	
	//@Hidden	
	/*public int getHashCode() {
		return hashCode();
	}*/
	
	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (hashCode() == o.hashCode()) return true;                
        
        return super.equals(o);
    }
	
	

}
