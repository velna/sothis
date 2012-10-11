package org.sothis.dal.mongo;

import org.sothis.dal.Dao;

public interface MongoDao<E extends MongoEntity> extends Dao<E, String> {

}
