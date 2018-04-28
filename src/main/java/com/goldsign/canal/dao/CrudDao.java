package com.goldsign.canal.dao;

import com.goldsign.canal.entity.BaseCanalEntity;

public interface CrudDao<E> {
	public void insert(BaseCanalEntity<E> entity);
	
	public void update(BaseCanalEntity<E> entity);
	
	public void delete(BaseCanalEntity<E> entity);
	
	public E select(BaseCanalEntity<E> entity);
}
