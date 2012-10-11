package org.sothis.web.example.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.sothis.dal.sql.AbstractSqlDao;
import org.sothis.dal.sql.SqlEntity;

public class BaseExampleDao<E extends SqlEntity, K extends Serializable> extends AbstractSqlDao<E, K> {

	private EntityManager entityManager;

	@Override
	protected EntityManager getEntityManager() {
		return entityManager;
	}

	@PersistenceContext(unitName = "test")
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
