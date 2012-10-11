package org.sothis.dal.mongo;

import org.sothis.dal.Entity;

public interface MongoEntity extends Entity {
	String getId();

	void setId(String id);
}
