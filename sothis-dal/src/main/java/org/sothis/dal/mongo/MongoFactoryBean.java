package org.sothis.dal.mongo;

import org.springframework.beans.factory.FactoryBean;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoFactoryBean implements FactoryBean<Mongo> {

	private String mongoUri;

	@Override
	public Mongo getObject() throws Exception {
		return new MongoClient(new MongoClientURI(this.mongoUri));
	}

	@Override
	public Class<?> getObjectType() {
		return Mongo.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setMongoUri(String mongoUri) {
		this.mongoUri = mongoUri;
	}

}
