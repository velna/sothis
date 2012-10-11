package org.sothis.dal.sql;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.sothis.core.util.Pager;
import org.sothis.dal.AbstractJpaCompatibleDao;
import org.sothis.dal.query.Chain;
import org.sothis.dal.query.Cnd;

public abstract class AbstractSqlDao<E extends SqlEntity, K extends Serializable> extends AbstractJpaCompatibleDao<E, K> implements SqlDao<E, K> {

	protected abstract EntityManager getEntityManager();

	@SuppressWarnings("unchecked")
	@Override
	public List<E> find(Cnd cnd, Pager pager, Chain chain) {
		Query query = SqlQueryBuilder.select(entityClass, cnd, chain, getEntityManager());

		if (null != pager) {
			query.setFirstResult(pager.getStartRow());
			query.setMaxResults(pager.getPageSize());
		} else {
			query.setFirstResult(0);
			query.setMaxResults(Integer.MAX_VALUE);
		}

		return query.getResultList();
	}

	@Override
	public E findById(K id) {
		return this.getEntityManager().find(entityClass, id);
	}

	@Override
	public int update(Cnd cnd, Chain chain) {
		Query query = SqlQueryBuilder.update(entityClass, cnd, chain, getEntityManager());
		return query.executeUpdate();
	}

	@Override
	public E update(E entity) {
		return this.getEntityManager().merge(entity);
	}

	@Override
	public int delete(Cnd cnd) {
		Query query = SqlQueryBuilder.delete(entityClass, cnd, getEntityManager());
		return query.executeUpdate();
	}

	@Override
	public E insert(E entity) {
		this.getEntityManager().persist(entity);
		return entity;
	}

	@Override
	public int count(Cnd cnd) {
		Query query = SqlQueryBuilder.count(entityClass, cnd, getEntityManager());
		Object result = query.getSingleResult();
		int ret = 0;
		if (result != null) {
			ret = Integer.parseInt(result.toString());
		}
		return ret;
	}

}
