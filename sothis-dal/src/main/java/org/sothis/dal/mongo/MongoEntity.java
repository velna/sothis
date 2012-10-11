package org.sothis.dal.mongo;

import org.sothis.dal.Entity;

/**
 * mongodb的实体类接口
 * 
 * @author velna
 * 
 */
public interface MongoEntity extends Entity {
	String getId();

	void setId(String id);
}
