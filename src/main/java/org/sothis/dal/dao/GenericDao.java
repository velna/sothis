package org.sothis.dal.dao;

import java.io.Serializable;
import java.util.List;

import org.sothis.dal.QueryBuilder;

public interface GenericDao<E extends Serializable, K extends Serializable> {
	int count();

	int count(QueryBuilder queryBuilder);

	int deleteById(K id);

	int delete(E entity);

	int delete(QueryBuilder queryBuilder);

	int delete(QueryBuilder queryBuilder, int maxResults);

	int delete(QueryBuilder queryBuilder, int firstResult, int maxResults);

	List<E> list();

	List<E> list(int maxResults);

	List<E> list(int firstResult, int maxResults);

	List<E> list(QueryBuilder queryBuilder);

	List<E> list(QueryBuilder queryBuilder, int maxResults);

	List<E> list(QueryBuilder queryBuilder, int firstResult, int maxResults);

	void save(E entity);

	void save(List<E> entityList);

	E unique(K id);

	E unique(QueryBuilder queryBuilder);

	E update(E entity);

	int update(List<E> entityList);

	int update(QueryBuilder queryBuilder);

	int update(QueryBuilder queryBuilder, int maxResults);

	int update(QueryBuilder queryBuilder, int firstResult, int maxResults);

	Class<E> getEntityClass();
}
