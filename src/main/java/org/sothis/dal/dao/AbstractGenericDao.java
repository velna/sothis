package org.sothis.dal.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.sothis.dal.DeleteQueryBuilder;
import org.sothis.dal.Operator;
import org.sothis.dal.QueryBuilder;
import org.sothis.dal.SelectQueryBuilder;

public abstract class AbstractGenericDao<E extends Serializable, K extends Serializable>
		implements GenericDao<E, K> {

	private static final Logger logger = Logger
			.getLogger(AbstractGenericDao.class);
	private EntityManagerFactory entityManagerFactory;
	private Class<E> entityClass;

	@Override
	public int count() {
		return count(SelectQueryBuilder.createCount(this.getEntityClass()));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int count(QueryBuilder queryBuilder) {
		Query query = queryBuilder.createQuery(this.getEntityManager());
		List result = query.getResultList();
		int ret = 0;
		if (null != result && !result.isEmpty()) {
			ret = Integer.parseInt(result.get(0).toString());
		}
		return ret;
	}

	@Override
	public int delete(E entity) {
		this.getEntityManager().remove(entity);
		return 1;
	}

	@Override
	public int deleteById(K id) {
		DeleteQueryBuilder queryBuilder = new DeleteQueryBuilder(
				this.getEntityClass());
		queryBuilder.where("id", Operator.Equal, id);
		return delete(queryBuilder);
	}

	@Override
	public int delete(QueryBuilder queryBuilder, int firstResult, int maxResults) {
		Query query = queryBuilder.createQuery(this.getEntityManager());
		query.setFirstResult(firstResult);
		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}
		return query.executeUpdate();
	}

	@Override
	public int delete(QueryBuilder queryBuilder, int maxResults) {
		return delete(queryBuilder, 0, maxResults);
	}

	@Override
	public int delete(QueryBuilder queryBuilder) {
		return delete(queryBuilder, 0, -1);
	}

	@Override
	public List<E> list() {
		return list(0, -1);
	}

	@Override
	public List<E> list(int maxResults) {
		return list(0, maxResults);
	}

	@Override
	public List<E> list(int firstResult, int maxResults) {
		return list(SelectQueryBuilder.create(this.getEntityClass()),
				firstResult, maxResults);
	}

	@Override
	public List<E> list(QueryBuilder queryBuilder) {
		return list(queryBuilder, 0, -1);
	}

	@Override
	public List<E> list(QueryBuilder queryBuilder, int maxResults) {
		return list(queryBuilder, 0, maxResults);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> list(QueryBuilder queryBuilder, int firstResult,
			int maxResults) {
		Query query = queryBuilder.createQuery(this.getEntityManager());
		query.setFirstResult(firstResult);
		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}
		return query.getResultList();
	}

	@Override
	public void save(E entity) {
		this.getEntityManager().persist(entity);
	}

	@Override
	public void save(List<E> entityList) {
		for (E entity : entityList) {
			try {
				save(entity);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	@Override
	public E unique(K id) {
		E ret = this.getEntityManager().find(this.getEntityClass(), id);
		return ret;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public E unique(QueryBuilder queryBuilder) {
		Query query = queryBuilder.createQuery(this.getEntityManager());
		query.setMaxResults(1);
		List result = query.getResultList();
		E ret = null;
		if (null != result && !result.isEmpty()) {
			ret = (E) result.get(0);
		}
		return ret;
	}

	@Override
	public E update(E entity) {
		return this.getEntityManager().merge(entity);
	}

	@Override
	public int update(List<E> entityList) {
		int ret = 0;
		for (E entity : entityList) {
			try {
				update(entity);
				ret++;
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		return ret;
	}

	@Override
	public int update(QueryBuilder queryBuilder) {
		return update(queryBuilder, 0, -1);
	}

	@Override
	public int update(QueryBuilder queryBuilder, int maxResults) {
		return update(queryBuilder, 0, maxResults);
	}

	@Override
	public int update(QueryBuilder queryBuilder, int firstResult, int maxResults) {
		Query query = queryBuilder.createQuery(this.getEntityManager());
		query.setFirstResult(firstResult);
		if (maxResults > 0) {
			query.setMaxResults(maxResults);
		}
		return query.executeUpdate();
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@SuppressWarnings("unchecked")
	public Class<E> getEntityClass() {
		if (null == entityClass) {
			ParameterizedType type = (ParameterizedType) this.getClass()
					.getGenericSuperclass();
			Type[] ts = type.getActualTypeArguments();
			entityClass = (Class<E>) ts[0];
		}
		return entityClass;
	}

	public abstract EntityManager getEntityManager();

}
