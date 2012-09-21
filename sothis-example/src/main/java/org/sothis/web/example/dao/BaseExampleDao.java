package org.sothis.web.example.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.sothis.dal.Entity;
import org.sothis.dal.query.sql.SqlDao;

public class BaseExampleDao<E extends Entity<K>, K extends Serializable> extends SqlDao<E, K> {

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
