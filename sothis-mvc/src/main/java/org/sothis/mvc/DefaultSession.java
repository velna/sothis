package org.sothis.mvc;

import java.util.Date;

public class DefaultSession implements Session {

	private final String id;
	private final Date dateCreated;
	private final Attributes attributes = new HashMapAttributes();

	public DefaultSession(String id) {
		this.id = id;
		dateCreated = new Date();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Date getDateCreated() {
		return dateCreated;
	}

	@Override
	public Date getLastAccessed() {
		return dateCreated;
	}

	@Override
	public Attributes attributes() {
		return attributes;
	}

}
