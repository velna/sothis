package org.sothis.mvc;

import java.util.Date;

public class DefaultSession implements Session {

	private final String id;
	private final Date dateCreated;
	private final Attributes attributes = new HashMapAttributes();
	private boolean invalidated;

	public DefaultSession(String id) {
		this.id = id;
		dateCreated = new Date();
	}

	@Override
	public String getId() {
		if (invalidated) {
			throw new IllegalStateException();
		}
		return id;
	}

	@Override
	public Date getDateCreated() {
		if (invalidated) {
			throw new IllegalStateException();
		}
		return dateCreated;
	}

	@Override
	public Date getLastAccessed() {
		if (invalidated) {
			throw new IllegalStateException();
		}
		return dateCreated;
	}

	@Override
	public Attributes attributes() {
		if (invalidated) {
			throw new IllegalStateException();
		}
		return attributes;
	}

	@Override
	public void invalidate() {
		if (invalidated) {
			throw new IllegalStateException();
		}
		invalidated = true;
	}

}
