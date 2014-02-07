package org.sothis.dal.mongo;

import org.sothis.dal.Dao;

/**
 * mongo db Dao的接口，以后可能会扩展一些mongo db特有的方法
 * 
 * @author velna
 * 
 * @param <E>
 */
public interface MongoDao<E extends MongoEntity> extends Dao<E, String> {

}
