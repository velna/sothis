package org.sothis.dal;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public interface QueryBuilder {
	public Query createQuery(EntityManager entityManager);
}
