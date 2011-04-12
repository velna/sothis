package org.sothis.dal.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.springframework.orm.jpa.EntityManagerFactoryUtils;

public class GenericDaoImpl<E extends Serializable, K extends Serializable>
		extends AbstractGenericDao<E, K> {

	@Override
	public EntityManager getEntityManager() {
		return EntityManagerFactoryUtils.getTransactionalEntityManager(this
				.getEntityManagerFactory());
	}

}
