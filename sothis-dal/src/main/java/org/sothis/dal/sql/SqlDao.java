package org.sothis.dal.sql;

import java.io.Serializable;

import org.sothis.dal.Dao;

public interface SqlDao<E extends SqlEntity, K extends Serializable> extends Dao<E, K> {

}
