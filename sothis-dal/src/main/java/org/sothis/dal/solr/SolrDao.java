package org.sothis.dal.solr;

import java.io.Serializable;

import org.sothis.dal.Dao;

public interface SolrDao<E extends SolrEntity, K extends Serializable> extends Dao<E, K> {

}
